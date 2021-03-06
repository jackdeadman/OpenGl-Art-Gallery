#version 330 core

in vec3 fragPos;
in vec3 ourNormal;
in vec2 ourTexCoord;

out vec4 fragColor;

uniform vec3 viewPos;

struct PointLight {
    vec3 position;

    float constant;
    float linear;
    float quadratic;

    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
};

struct Material {
  sampler2D diffuse;
  sampler2D specular;
  float shininess;
};

struct DirectionalLight {
    vec3 direction;

    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
};

uniform DirectionalLight dirLight;

#define NR_POINT_LIGHTS 2
uniform PointLight pointLights[NR_POINT_LIGHTS];

uniform Material material;

vec3 calcDirLight(DirectionalLight light, vec3 normal, vec3 viewDir)
{
    // Flip the vector
    vec3 lightDir = normalize(-light.direction);
    // diffuse shading
    float diff = max(dot(normal, lightDir), 0.0);
    // specular shading
    vec3 reflectDir = reflect(-lightDir, normal);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.shininess);

    // combine results
    vec3 ambient  = light.ambient  * vec3(texture(material.diffuse, ourTexCoord));
    vec3 diffuse  = light.diffuse  * diff * vec3(texture(material.diffuse, ourTexCoord));
    vec3 specular = light.specular * spec * vec3(texture(material.specular, ourTexCoord));
    return (ambient + diffuse + specular);
}


// calculates the color when using a point light.
vec3 CalcPointLight(PointLight light, vec3 norm, vec3 fragPos)
{
      vec3 ambient = light.ambient * vec3(texture(material.diffuse, ourTexCoord));
      vec3 lightDir = normalize(light.position - fragPos);
      float diff = max(dot(norm, lightDir), 0.0);

      vec3 diffuse = light.diffuse * diff * vec3(texture(material.diffuse, ourTexCoord));

      // specular
      vec3 viewDir = normalize(viewPos - fragPos);
      vec3 reflectDir = reflect(-lightDir, norm);
      float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.shininess);
      vec3 specular = light.specular * spec * vec3(texture(material.specular, ourTexCoord));


      float distance    = length(light.position - fragPos);
      float attenuation = 1.0 / (light.constant + light.linear * distance +
        		    light.quadratic * (distance * distance));

      ambient  *= attenuation;
      diffuse  *= attenuation;
      specular *= attenuation;
      return (ambient + diffuse + specular);
}


void main() {

  vec3 norm = normalize(ourNormal);
  vec3 viewDir = normalize(viewPos - fragPos);
  vec3 result = calcDirLight(dirLight, ourNormal, viewDir);

  // phase 2: point lights
  for(int i = 0; i < 2; i++) {
      result += CalcPointLight(pointLights[i], norm, fragPos);
  }

  fragColor = vec4(result, 1.0);

}
