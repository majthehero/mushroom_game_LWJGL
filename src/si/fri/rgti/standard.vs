varying vec3 viewVector;
varying vec3 wNormalInterp;
varying vec3 vertPos;
varying vec2 texcoord;

attribute vec2 uv2;

void main(){
	gl_Position = projectionMatrix * modelViewMatrix * vec4(position, 1.0);
	vec4 vertPos4 = modelMatrix * vec4(position, 1.0);

	vertPos = vec3(vertPos4) / vertPos4.w;
	viewVector = position - cameraPosition;
	wNormalInterp = normal;

	texcoord = uv;
}
