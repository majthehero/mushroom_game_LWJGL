uniform mat4 viewProjMat;
uniform mat4 worldMat;

attribute vec3 position;
attribute vec3 normal;
attribute vec2 texCoord;

varying vec3 v_position;
varying vec3 v_normal;
varying vec2 v_texCoord;

void
main()
{
    gl_Position = viewProjMat * worldMat * vec4(position, 1.0);
    v_position = (worldMat * vec4(position, 1.0)).xyz;
    v_normal = mat3(worldMat) * normal;
    v_texCoord = texCoord;
}