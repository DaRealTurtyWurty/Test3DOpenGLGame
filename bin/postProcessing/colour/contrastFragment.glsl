#version 140

in vec2 textureCoords;

out vec4 out_Colour;

uniform sampler2D colourTexture;
uniform vec4 permutation;
uniform float contrast;

void main(void) {
  vec4 tex = texture(colourTexture, textureCoords);

  // cycle through the 4 values in permutation
  for (int i = 0; i < 4; i++) {
    switch (i) {
    case 0:
      switch (int(permutation.x)) {
      case 0:
        out_Colour.x = tex.r;
        break;
      case 1:
        out_Colour.x = tex.g;
        break;
      case 2:
        out_Colour.x = tex.b;
        break;
      case 3:
        out_Colour.x = tex.a;
        break;
      }
      break;

    case 1:
      switch (int(permutation.y)) {
      case 0:
        out_Colour.y = tex.r;
        break;
      case 1:
        out_Colour.y = tex.g;
        break;
      case 2:
        out_Colour.y = tex.b;
        break;
      case 3:
        out_Colour.y = tex.a;
        break;
      }
      break;

    case 2:
      switch (int(permutation.z)) {
      case 0:
        out_Colour.z = tex.r;
        break;
      case 1:
        out_Colour.z = tex.g;
        break;
      case 2:
        out_Colour.z = tex.b;
        break;
      case 3:
        out_Colour.z = tex.a;
        break;
      }
      break;

    case 3:
      switch (int(permutation.w)) {
      case 0:
        out_Colour.w = tex.r;
        break;
      case 1:
        out_Colour.w = tex.g;
        break;
      case 2:
        out_Colour.w = tex.b;
        break;
      case 3:
        out_Colour.w = tex.a;
        break;
      }
      break;
    }
  }
  
  out_Colour.rgb = (out_Colour.rgb - 0.5) * (1.0 + contrast) + 0.5;
}
