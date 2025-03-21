#version 400 core

in vec2 pass_textureCoordinates;
in vec3 surfaceNormal;
in vec3 toLightVector[4];
in vec3 toCameraVector;
in float visibility;
in vec4 shadowCoords;

layout(location = 0) out vec4 out_Color;
layout(location = 1) out vec4 out_BrightColor;

uniform sampler2D modelTexture;
uniform sampler2D shadowMap;
uniform sampler2D specularMap;
uniform float usesSpecularMap;
uniform vec3 lightColour[4];
uniform vec3 attenuation[4];
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColour;
uniform float shadowMapSize;

const int pcfCount = 2;
const float totalTexels = (pcfCount * 2.0 + 1.0) * (pcfCount * 2.0 + 1.0);

void main(void) {

  float texelSize = 1.0 / shadowMapSize;
  float total = 0.0;
  for (int x = -pcfCount; x < pcfCount; x++) {
    for (int y = -pcfCount; y < pcfCount; y++) {
      float objectNearestLight =
          texture(shadowMap, shadowCoords.xy + vec2(x, y) * texelSize).r;
      if (shadowCoords.z > objectNearestLight + 0.002) {
        total += 1.0;
      }
    }
  }

  total /= totalTexels;

  float lightFactor = 1.0 - (total * shadowCoords.w);

  vec3 unitNormal = normalize(surfaceNormal);
  vec3 unitVectorToCamera = normalize(toCameraVector);

  vec3 totalDiffuse = vec3(0.0);
  vec3 totalSpecular = vec3(0.0);

  for (int i = 0; i < 4; i++) {
    float distance = length(toLightVector[i]);
    float attenFactor = (attenuation[i].x + attenuation[i].y * distance) +
                        attenuation[i].z * distance * distance;
    vec3 unitLightVector = normalize(toLightVector[i]);
    float nDotl = dot(unitNormal, unitLightVector);
    float brightness = max(nDotl, 0.0);
    vec3 lightDirection = -unitLightVector;
    vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);
    float specularFactor = dot(reflectedLightDirection, unitVectorToCamera);
    specularFactor = max(specularFactor, 0.0);
    float dampedFactor = pow(specularFactor, shineDamper);
    totalDiffuse += (brightness * lightColour[i]) / attenFactor;
    totalSpecular +=
        (dampedFactor * reflectivity * lightColour[i]) / attenFactor;
  }
  totalDiffuse = max(totalDiffuse * lightFactor, 0.4);

  vec4 textureColour = texture(modelTexture, pass_textureCoordinates);
  if (textureColour.a < 0.5) {
    discard;
  }

  out_BrightColor = vec4(0.0);
  if (usesSpecularMap > 0.5f) {
    vec4 mapInfo = texture(specularMap, pass_textureCoordinates);
    totalSpecular *= mapInfo.r;
    if (mapInfo.g > 0.5) {
      out_BrightColor = textureColour + vec4(totalSpecular, 1.0);
      totalDiffuse = vec3(1.0);
    }
  }

  out_Color =
      vec4(totalDiffuse, 1.0) * textureColour + vec4(totalSpecular, 1.0);
  out_Color = mix(vec4(skyColour, 1.0), out_Color, visibility);
}
