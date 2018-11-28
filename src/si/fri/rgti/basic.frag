uniform sampler2D s_tex0;
uniform vec3 lightPos;
uniform vec3 Sd;

varying vec3 v_position;
varying vec3 v_normal;
varying vec2 v_texCoord;

vec3 phongDiffuse(vec3 Md); /* Forward declaration */

void
main()
{
    vec3 Md = texture2D(s_tex0, v_texCoord).xyz;
    vec3 phongColor = phongDiffuse(Md);

    gl_FragColor = vec4(phongColor, 1.0);
}

vec3
phongDiffuse(vec3 Md) {
    /* Diffuse term of Phong model per fragment */
    vec3 lightDirection = normalize(lightPos - v_position);
    vec3 nor = normalize(v_normal);

    /* Diffuse term */
    float Diffuse = max(dot(nor, lightDirection), 0.0);
    vec3 PhongDiffuse = Diffuse * Sd * Md;

    return PhongDiffuse;
}