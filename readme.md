# 总览

以往web开发中，编写`controller`层的conVO对象代码占据了大量时间，如果我们能直接用接口yaml文件生成这些对象就好了，这样我们编写的yaml文件可以直接生成`controller`层以及VO对象，我们也可以把yaml文件直接提供给调用方，生成http客户端代码发起服务调用。本工程提供了利用`openapitools`利用yaml文件直接生成`controller`代码以及`httpclient`客户端，符合API-First开发理念。

# 什么是API-First开发理念

API-First（API 优先）是一种 **以 API 为核心驱动的软件开发模式**—— 在编写任何业务代码前，先设计、定义并冻结 API 规范（如 OpenAPI/Swagger 规范），再以该规范为 “契约”，同步推进前后端、跨语言（如 Java/C++）开发，最终所有模块通过统一 API 对接。

简单说：**先定 “接口契约”，再写代码**，而非传统 “先写后端代码，再凑接口文档，最后前端适配” 的模式。

# OpenAPITools介绍

OpenAPI Generator 能够根据 OpenAPI 规范（同时支持 2.0 和 3.0 版本），自动生成 API 客户端库（SDK 生成）、服务端桩代码（接口骨架）、文档及配置文件。[OpenAPITools主页](https://github.com/OpenAPITools/openapi-generator)

# OpenAPI Generator快速入门

本章节我们将演示如何通过一个swagger的yaml接口文件生成controller以及okhttp/feign等客户端代码。使用`jdk21`

项目已在github上开源

## 新建项目工程

项目工程结构如下：

```shell
|---java-openapi-generator
	|---pom.xm								# 父pom文件，定义三方件的依赖版本
	|---java-openapi-controller				# 服务端模块，生成controller层代码
		|---src/main/java					# 服务端代码
		|---src/main/resource				# 资源文件
			|---openapi
				|---v1
					|---openapi.yaml		# 接口yaml，用于生成服务端controller与客户端代码
	|---java-openapi-sdk					# 客户端模块，生成okhttp/feign等客户端代码
```

## 配置maven依赖

```xml
```

## 测试yaml文件

这里我们可以使用OpenAPI Generator样例中的yaml示例也可以使用自己已经写好的yaml，这里使用OpenAPI Generator样例中的`ping some object`示例并加tag为curl的相关方法

[OpenAPI Generator样例](https://github.com/OpenAPITools/openapi-generator/tree/master/samples/client/others/java/okhttp-gson-streaming/api)

```yaml
openapi: 3.0.1
info:
  title: ping some object
  version: "1.0"
servers:
  - url: http://localhost:8082/
tags:
  - ping
  - curl
paths:
  /ping:
    get:
      operationId: getPing
      parameters:
        - description: ID of pet that needs to be updated
          explode: true
          in: query
          name: petId
          required: true
          schema:
            format: int64
            type: integer
          style: form
      requestBody:
        content:
          application/x-www-form-urlencoded:
            schema:
              $ref: "#/components/schemas/getPing_request"
      responses:
        "200":
          content:
            application/json:
              schema:
                $ref: "#/components/schemas/SomeObj"
          description: OK
      tags:
        - ping
      x-streaming: true
      x-group-parameters: true
      x-content-type: application/x-www-form-urlencoded
      x-accepts:
        - application/json
    post:
      operationId: postPing
      requestBody:
        content:
          application/json:
            schema:
              $ref: "#/components/schemas/SomeObj"
      responses:
        "200":
          content:
            application/json:
              schema:
                $ref: "#/components/schemas/SomeObj"
          description: OK
      tags:
        - ping
      x-streaming: true
      x-content-type: application/json
      x-accepts:
        - application/json
  /curl:
    get:
      operationId: getCurl
      parameters:
        - description: ID of pet that needs to be updated
          explode: true
          in: query
          name: petId
          required: true
          schema:
            format: int64
            type: integer
          style: form
      requestBody:
        content:
          application/x-www-form-urlencoded:
            schema:
              $ref: "#/components/schemas/getCurl_request"
      responses:
        "200":
          content:
            application/json:
              schema:
                $ref: "#/components/schemas/SomeObj"
          description: OK
      tags:
        - curl
      x-streaming: true
      x-group-parameters: true
      x-content-type: application/x-www-form-urlencoded
      x-accepts:
        - application/json
    post:
      operationId: postCurl
      requestBody:
        content:
          application/json:
            schema:
              $ref: "#/components/schemas/SomeObj"
      responses:
        "200":
          content:
            application/json:
              schema:
                $ref: "#/components/schemas/SomeObj"
          description: OK
      tags:
        - ping
      x-streaming: true
      x-content-type: application/json
      x-accepts:
        - application/json
components:
  schemas:
    SomeObj:
      example:
        name: name
        active: true
        $_type: SomeObjIdentifier
        id: 0
        type: type
      properties:
        $_type:
          default: SomeObjIdentifier
          enum:
            - SomeObjIdentifier
          type: string
        id:
          format: int64
          type: integer
        name:
          type: string
        active:
          type: boolean
        type:
          type: string
      type: object
    SimpleOneOf:
      oneOf:
        - type: string
        - type: integer
    getPing_request:
      properties:
        name:
          description: Updated name of the pet
          type: string
        status:
          description: Updated status of the pet
          type: string
      type: object
    getCurl_request:
      properties:
        name:
          description: Updated name of the pet
          type: string
        status:
          description: Updated status of the pet
          type: string
      type: object


```

## 使用maven插件生成服务端代码

openapi-generator-maven-plugin官网指导

https://github.com/OpenAPITools/openapi-generator/blob/master/modules/openapi-generator-maven-plugin/README.md

工程中配置如下：使用`maven-clean-plugin`插件自动清理生成的文件，使用`openapi-generator-maven-plugin`生成服务端代码

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-clean-plugin</artifactId>
            <configuration>
                <filesets>
                    <fileset>
                        <directory>${project.basedir}/src/main/java/org/org/numb/openapi/generator/gen</directory>
                    </fileset>
                </filesets>
            </configuration>
        </plugin>
        <plugin>
            <groupId>org.openapitools</groupId>
            <artifactId>openapi-generator-maven-plugin</artifactId>
            <!-- RELEASE_VERSION -->
            <version>${openapi-generator-maven-plugin.version}</version>
            <executions>
                <execution>
                    <id>generate-controller</id>
                    <phase>generate-resources</phase>
                    <goals>
                        <goal>generate</goal>
                    </goals>
                    <configuration>
                        <!-- 1. 关键配置：OpenAPI 规范文件（本地路径或 URL） -->
                        <inputSpec>${project.basedir}/src/main/resources/openapi/v1/openapi.yaml</inputSpec>
                        <!-- 或从远程 API 获取规范：<inputSpec>http://localhost:8080/v3/api-docs</inputSpec> -->

                        <!-- 2. 生成代码目标语言（必填，支持 50+ 语言，小写） -->
                        <!-- 常用语言：java、python、go、typescript-axios、php、csharp 等 -->
                        <generatorName>spring</generatorName>
                        <library>spring-boot</library>
                        <!-- 3. 输出目录（生成的代码存放位置，默认 src/main/java） -->
                        <output>${project.basedir}</output>
                        <!-- 4. 包配置（Java 语言专属，指定生成代码的包名） -->
                        <packageName>org.org.numb.openapi.generator.gen</packageName> <!-- 根包名 -->
                        <apiPackage>org.org.numb.openapi.generator.gen.delegate</apiPackage> <!-- API 操作类包名 -->
                        <modelPackage>org.org.numb.openapi.generator.gen.model</modelPackage> <!-- 数据模型包名 -->
                        <!-- 5. 额外配置（可选，根据语言自定义） -->
                        <generateModelTests>false</generateModelTests> <!-- 不生成模型测试代码 -->
                        <generateApiTests>false</generateApiTests> <!-- 不生成API测试代码 -->
                        <openapiGeneratorIgnoreList>pom.xml</openapiGeneratorIgnoreList>

                        <configOptions>
                            <interfaceOnly>true</interfaceOnly>
                            <dateLibrary>java21</dateLibrary> <!-- 日期处理用 Java8 LocalDate -->
                            <useJakartaEe>true</useJakartaEe> <!-- 是否使用 Jakarta EE（默认 false，用 Java EE） -->
                            <useBeanValidation>true</useBeanValidation>
                            <delegatePattern>true</delegatePattern>
                            <useSpringController>false</useSpringController>
                            <useOneOfInterfaces>true</useOneOfInterfaces>
                        </configOptions>
                        <!-- 6. 忽略已存在的文件（避免覆盖手动修改的代码，可选） -->
                        <ignoreFileOverride>${project.basedir}/</ignoreFileOverride>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

#### configuration常用配置

- `inputSpec`：OpenAPI 规范文件（本地路径或 URL）
- `generatorName`：生成代码目标语言，当生成服务端代码时使用`spring`，`library`支持`spring-boot`、`spring-cloud`等标签
- `library`：服务端常用`spring-boot`、`spring-cloud`。`spring-boot`为生成controller代码，如果指定`spring-cloud`还会生成feign的客户端接口
- `output`：输出目录（生成的代码存放位置，默认 src/main/java）
- `packageName`：根包名配置（Java 语言专属，指定生成代码的包名）
- `apiPackage`：API 操作类包名
- `modelPackage`： 数据模型包名
- `generateModelTests`：不生成模型测试代码
- `generateApiTests`：不生成API测试代码
- `openapiGeneratorIgnoreList`：不生成的文件列表，这里可以配置pom.xml，让我们可以重新定义依赖组件的版本，不由openapi-tools控制

#### configOptions常用配置

- `interfaceOnly`: 只生成controller接口代码，不生成实现类
- `useJakartaEe`：是否使用 Jakarta EE（默认 false，用 Java EE），使用jdk17/jdk21与springboot3.x设置为true
- `useBeanValidation`：是否开启bean校验
- `delegatePattern`：是否使用代理模式生成服务端代码

spring服务端更多配置参考：

https://openapi-generator.tech/docs/generators/spring/#config-options

客户端配置与服务端配置不同，可根据生成代码需求按照generator类型分类查找

https://openapi-generator.tech/docs/generators/





# OpenAPI Generator高级配置

## CONFIG OPTIONS 配置选项

这些选项可以作为附加属性（命令行界面）或配置选项（插件）应用。有关更多详细信息，请参阅[配置文档](https://openapi-generator.tech/docs/configuration)。

| Option 选项                                                  | Description 描述                                             | Values 值                                                    | Default 默认                                                 |
| ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| additionalEnumTypeAnnotations 额外的枚举类型注解             | Additional annotations for enum type(class level annotations)枚举类型的附加注解（类级注解） |                                                              | null                                                         |
| additionalModelTypeAnnotations                               | Additional annotations for model type(class level annotations). List separated by semicolon(;) or new line (Linux or Windows)模型类型的附加注释（类级注释）。用分号（;）或换行符（Linux或Windows）分隔的列表 |                                                              | null                                                         |
| additionalOneOfTypeAnnotations oneOf类型的附加注解           | Additional annotations for oneOf interfaces(class level annotations). List separated by semicolon(;) or new line (Linux or Windows)oneOf接口的附加注释（类级注释）。列表用分号（;）或换行符（Linux或Windows）分隔 |                                                              | null                                                         |
| allowUnicodeIdentifiers 允许Unicode标识符                    | boolean, toggles whether unicode identifiers are allowed in names or not, default is false布尔值，用于切换名称中是否允许使用Unicode标识符，默认值为false |                                                              | false 假                                                     |
| annotationLibrary 注释库                                     | Select the complementary documentation annotation library.选择互补的文档注释库。 | **none **：Do not annotate Model and Api with complementary annotations.不要用互补注释来标注模型和应用程序接口。<br />**swagger1**：Annotate Model and Api using the Swagger Annotations 1.x library.使用Swagger Annotations 1.x库对模型和API进行注释。<br />**swagger2**：Annotate Model and Api using the Swagger Annotations 2.x library.使用Swagger Annotations 2.x库对模型和API进行注释。 | none 无                                                      |
| apiPackage api包                                             | package for generated api classes 用于生成API类的包          |                                                              | org.openapitools.client.api 组织。打开api工具。客户端。api   |
| artifactDescription artifact描述                             | artifact description in generated pom.xml生成的pom.xml中的构件描述 |                                                              | OpenAPI Java                                                 |
| artifactId                                                   | artifactId in generated pom.xml. This also becomes part of the generated library's filename生成的pom.xml中的artifactId。这也会成为生成的库文件名的一部分 |                                                              | openapi-java-client openapi-java客户端                       |
| artifactUrl 构件URL                                          | artifact URL in generated pom.xml 生成的pom.xml中的构件URL   |                                                              | https://github.com/openapitools/openapi-generator            |
| artifactVersion 构件版本                                     | artifact version in generated pom.xml. This also becomes part of the generated library's filename. If not provided, uses the version from the OpenAPI specification file. If that's also not present, uses the default value of the artifactVersion option.生成的pom.xml中的构件版本。这也会成为生成的库文件名的一部分。如果未提供，将使用OpenAPI规范文件中的版本。如果该文件中也没有版本，则使用artifactVersion选项的默认值。 |                                                              | 1.0.0                                                        |
| asyncNative 异步原生                                         | If true, async handlers will be used, instead of the sync version如果为true，则将使用异步处理程序，而非同步版本。 |                                                              | false 假                                                     |
| bigDecimalAsString bigDecimal作为字符串                      | Treat BigDecimal values as Strings to avoid precision loss.将BigDecimal值视为字符串以避免精度损失。 |                                                              | false 假                                                     |
| booleanGetterPrefix                                          | Set booleanGetterPrefix 设置布尔值获取器前缀                 |                                                              | get 获取                                                     |
| camelCaseDollarSign 美元符号驼峰式命名                       | Fix camelCase when starting with $ sign. when true : $Value when false : $value修复以$符号开头时的驼峰式命名。当为true时：$Value；当为false时：$value |                                                              | false 假                                                     |
| caseInsensitiveResponseHeaders                               | Make API response's headers case-insensitive. Available on okhttp-gson, jersey2 libraries使API响应的标头不区分大小写。适用于okhttp-gson、jersey2库 |                                                              | false 假                                                     |
| configKey 配置键                                             | Config key in @RegisterRestClient. Default to none. Only `microprofile` supports this option.@RegisterRestClient中的配置键。默认为无。仅`microprofile`支持此选项。 |                                                              | null                                                         |
| configKeyFromClassName 从类名获取配置键                      | If true, set tag as key in @RegisterRestClient. Default to false. Only `microprofile` supports this option.如果为true，则在@RegisterRestClient中将标签设置为键。默认为false。只有`microprofile`支持此选项。 |                                                              | null                                                         |
| containerDefaultToNull                                       | Set containers (array, set, map) default to null将容器（数组、集合、映射）默认设置为 null |                                                              | false 假                                                     |
| dateLibrary 日期库                                           | Option. Date library to use 选项。要使用的日期库             | **joda 碘**Joda (for legacy app only) Joda（仅适用于遗留应用程序）**legacy 遗留**Legacy java.util.Date 传统的 java.util.Date**java8-localdatetime java8-localtime**Java 8 using LocalDateTime (for legacy app only)Java 8 使用 LocalDateTime（仅适用于遗留应用程序）**java8**Java 8 native JSR310 (preferred for jdk 1.8+)Java 8 原生 JSR310（推荐用于 jdk 1.8+） | java8                                                        |
| developerEmail 开发者邮箱                                    | developer email in generated pom.xml生成的pom.xml中的开发者电子邮件 |                                                              | [team@openapitools.org](mailto:team@openapitools.org)        |
| developerName 开发者名称                                     | developer name in generated pom.xml 生成的pom.xml中的开发者名称 |                                                              | OpenAPI-Generator Contributors OpenAPI生成器贡献者           |
| developerOrganization 开发者组织                             | developer organization in generated pom.xml生成的pom.xml中的开发者组织 |                                                              | OpenAPITools.org                                             |
| developerOrganizationUrl 开发者组织网址                      | developer organization URL in generated pom.xml生成的pom.xml中的开发者组织URL |                                                              | [http://openapitools.org](http://openapitools.org/)          |
| disableHtmlEscaping 禁用HTML转义                             | Disable HTML escaping of JSON strings when using gson (needed to avoid problems with byte[] fields)使用gson时禁用JSON字符串的HTML转义（这是避免byte[]字段出现问题所必需的） |                                                              | false 假                                                     |
| disallowAdditionalPropertiesIfNotPresent不存在时禁止额外属性 | If false, the 'additionalProperties' implementation (set to true by default) is compliant with the OAS and JSON schema specifications. If true (default), keep the old (incorrect) behaviour that 'additionalProperties' is set to false by default.如果为 false，则“additionalProperties”的实现（默认设置为 true）符合 OAS 和 JSON 模式规范。如果为 true（默认值），则保留旧的（不正确的）行为，即“additionalProperties”默认设置为 false。 | **false **The 'additionalProperties' implementation is compliant with the OAS and JSON schema specifications.“additionalProperties”的实现符合OAS和JSON模式规范。<br />**true **Keep the old (incorrect) behaviour that 'additionalProperties' is set to false by default.保留“additionalProperties”默认设置为false这一旧的（不正确的）行为。 | true 真                                                      |
| discriminatorCaseSensitive 鉴别器区分大小写                  | Whether the discriminator value lookup should be case-sensitive or not. This option only works for Java API client判别器值查找是否区分大小写。此选项仅适用于Java API客户端 |                                                              | true 真                                                      |
| documentationProvider 文档提供程序                           | Select the OpenAPI documentation provider.选择OpenAPI文档提供商。 | **none **：不发布OpenAPI规范。<br />**source **：发布原始输入的OpenAPI规范。 | source 来源                                                  |
| dynamicOperations 动态操作                                   | Generate operations dynamically at runtime from an OAS在运行时从OAS动态生成操作 |                                                              | false 假                                                     |
| ensureUniqueParams 确保独一无二                              | Whether to ensure parameter names are unique in an operation (rename parameters that are not).是否确保操作中的参数名称唯一（重命名不唯一的参数）。 |                                                              | true                                                         |
| enumPropertyNaming 枚举属性命名                              | Naming convention for enum properties: 'MACRO_CASE', 'legacy' and 'original'枚举属性的命名约定：'MACRO_CASE'、'legacy' 和 'original |                                                              | MACRO_CASE 宏大写                                            |
| enumUnknownDefaultCase 枚举未知默认情况                      | If the server adds new enum cases, that are unknown by an old spec/client, the client will fail to parse the network response.With this option enabled, each enum will have a new case, 'unknown_default_open_api', so that when the server sends an enum case that is not known by the client/spec, they can safely fallback to this case.如果服务器添加了旧规范/客户端未知的新枚举值，客户端将无法解析网络响应。启用此选项后，每个枚举都会有一个新值“unknown_default_open_api”，这样当服务器发送客户端/规范未知的枚举值时，它们可以安全地回退到这个值。 | **false 假**No changes to the enum's are made, this is the default option.不对枚举进行任何更改，这是默认选项。**true 真**With this option enabled, each enum will have a new case, 'unknown_default_open_api', so that when the enum case sent by the server is not known by the client/spec, can safely be decoded to this case.启用此选项后，每个枚举都会有一个新的情况“unknown_default_open_api”，这样当服务器发送的枚举情况不为客户端/规范所知时，就可以安全地解码为此情况。 | false 假                                                     |
| errorObjectType                                              | Error Object type. (This option is for okhttp-gson only)错误对象类型。（此选项仅适用于okhttp-gson） |                                                              | null                                                         |
| failOnUnknownProperties 遇到未知属性时失败                   | Fail Jackson de-serialization on unknown properties遇到未知属性时，Jackson反序列化失败 |                                                              | false 假                                                     |
| generateBuilders 生成构建器                                  | Whether to generate builders for models是否为模型生成构建器  |                                                              | false 假                                                     |
| generateClientAsBean                                         | For resttemplate, restclient and webclient, configure whether to create `ApiClient.java` and Apis clients as bean (with `@Component` annotation).对于resttemplate、restclient和webclient，配置是否将`ApiClient.java`和Apis客户端创建为bean（带有`@Component`注解）。 |                                                              | false 假                                                     |
| generateConstructorWithAllArgs 生成包含所有参数的构造函数    | whether to generate a constructor for all arguments是否为所有参数生成构造函数 |                                                              | false 假                                                     |
| gradleProperties gradle属性                                  | Append additional Gradle properties to the gradle.properties file将额外的Gradle属性添加到gradle.properties文件中 |                                                              | null                                                         |
| groupId                                                      | groupId in generated pom.xml 生成的pom.xml中的groupId        |                                                              | org.openapitools                                             |
| hideGenerationTimestamp 隐藏生成时间戳                       | Hides the generation timestamp when files are generated.生成文件时隐藏生成时间戳。 |                                                              | false 假                                                     |
| ignoreAnyOfInEnum 枚举中忽略任意项                           | Ignore anyOf keyword in enum 忽略枚举中的anyOf关键字         |                                                              | false 假                                                     |
| implicitHeaders 隐式头部                                     | Skip header parameters in the generated API methods using @ApiImplicitParams annotation.使用@ApiImplicitParams注解在生成的API方法中跳过头部参数。 |                                                              | false 假                                                     |
| implicitHeadersRegex                                         | Skip header parameters that matches given regex in the generated API methods using @ApiImplicitParams annotation. Note: this parameter is ignored when implicitHeaders=true在使用@ApiImplicitParams注解生成的API方法中，跳过与给定正则表达式匹配的请求头参数。注意：当implicitHeaders=true时，此参数将被忽略。 |                                                              | null                                                         |
| invokerPackage 调用程序包                                    | root package for generated code 生成代码的根包               |                                                              | org.openapitools.client 组织。打开api工具。客户端            |
| legacyDiscriminatorBehavior 传统鉴别器行为                   | Set to false for generators with better support for discriminators. (Python, Java, Go, PowerShell, C# have this enabled by default).对于对鉴别器有更好支持的生成器，应将其设置为false。（Python、Java、Go、PowerShell、C#默认启用此功能）。 | **true**The mapping in the discriminator includes descendent schemas that allOf inherit from self and the discriminator mapping schemas in the OAS document.鉴别器中的映射包含所有从自身继承的子模式以及OAS文档中的鉴别器映射模式。<br />**false 假**The mapping in the discriminator includes any descendent schemas that allOf inherit from self, any oneOf schemas, any anyOf schemas, any x-discriminator-values, and the discriminator mapping schemas in the OAS document AND Codegen validates that oneOf and anyOf schemas contain the required discriminator and throws an error if the discriminator is missing.鉴别器中的映射包括allOf从自身继承的任何子模式、任何oneOf模式、任何anyOf模式、任何x-discriminator-values，以及OAS文档中的鉴别器映射模式，并且Codegen会验证oneOf和anyOf模式是否包含所需的鉴别器，如果鉴别器缺失则会抛出错误。 | true 真                                                      |
| library 库                                                   | **library template (sub-template) to use要使用的库模板（子模板）** | **jersey2**<br/><br/>HTTP client: Jersey client 2.25.1. JSON processing: Jackson 2.17.1<br/>HTTP客户端：Jersey客户端2.25.1。JSON处理：Jackson 2.17.1<br/>**jersey3**<br/>HTTP client: Jersey client 3.1.1. JSON processing: Jackson 2.17.1<br/>HTTP客户端：Jersey客户端3.1.1。JSON处理：Jackson 2.17.1<br/>**feign**<br/>HTTP client: OpenFeign 13.2.1. JSON processing: Jackson 2.17.1 or Gson 2.10.1<br/>HTTP客户端：OpenFeign 13.2.1。JSON处理：Jackson 2.17.1或Gson 2.10.1<br/>feign-hc5<br/>HTTP client: OpenFeign 13.2.1/HttpClient5 5.4.2. JSON processing: Jackson 2.17.1 or Gson 2.10.1<br/>HTTP客户端：OpenFeign 13.2.1/HttpClient5 5.4.2。JSON处理：Jackson 2.17.1或Gson 2.10.1<br/>okhttp-gson<br/>[DEFAULT] HTTP client: OkHttp 4.11.0. JSON processing: Gson 2.10.1. Enable Parcelable models on Android using '-DparcelableModel=true'. Enable gzip request encoding using '-DuseGzipFeature=true'.<br/>[默认] HTTP客户端：OkHttp 4.11.0。JSON处理：Gson 2.10.1。在Android上使用“-DparcelableModel=true”启用Parcelable模型。使用“-DuseGzipFeature=true”启用gzip请求编码。<br/>retrofit2<br/>HTTP client: OkHttp 4.11.0. JSON processing: Gson 2.10.1 (Retrofit 2.5.0) or Jackson 2.17.1. Enable the RxJava adapter using '-DuseRxJava[2/3]=true'. (RxJava 1.x or 2.x or 3.x)<br/>HTTP客户端：OkHttp 4.11.0。JSON处理：Gson 2.10.1（Retrofit 2.5.0）或Jackson 2.17.1。使用“-DuseRxJava[2/3]=true”启用RxJava适配器。（RxJava 1.x、2.x或3.x）<br/>resttemplate 休息模板<br/>HTTP client: Spring RestTemplate 5.3.33 (6.1.5 if useJakartaEe=true). JSON processing: Jackson 2.17.1<br/>HTTP客户端：Spring RestTemplate 5.3.33（若useJakartaEe=true，则为6.1.5）。JSON处理：Jackson 2.17.1<br/>**webclient**<br/>HTTP client: Spring WebClient 5.1.18. JSON processing: Jackson 2.17.1<br/>HTTP客户端：Spring WebClient 5.1.18。JSON处理：Jackson 2.17.1<br/>restclient rest客户端<br/>HTTP client: Spring RestClient 6.1.6. JSON processing: Jackson 2.17.1<br/>HTTP客户端：Spring RestClient 6.1.6。JSON处理：Jackson 2.17.1<br/>resteasy<br/>HTTP client: Resteasy client 4.7.6. JSON processing: Jackson 2.17.1<br/>HTTP客户端：Resteasy客户端4.7.6。JSON处理：Jackson 2.17.1<br/>vertx 顶点<br/>HTTP client: VertX client 3.5.2. JSON processing: Jackson 2.17.1<br/>HTTP客户端：VertX客户端3.5.2。JSON处理：Jackson 2.17.1<br/>google-api-client 谷歌API客户端<br/>HTTP client: Google API client 2.2.0. JSON processing: Jackson 2.17.1<br/>HTTP客户端：Google API客户端2.2.0。JSON处理：Jackson 2.17.1<br/>rest-assured REST保证<br/>HTTP client: rest-assured 5.3.2. JSON processing: Gson 2.10.1 or Jackson 2.17.1. Only for Java 8<br/>HTTP客户端：rest-assured 5.3.2。JSON处理：Gson 2.10.1或Jackson 2.17.1。仅适用于Java 8<br/>native 原生的<br/>HTTP client: Java native HttpClient. JSON processing: Jackson 2.17.1. Only for Java11+<br/>HTTP客户端：Java原生HttpClient。JSON处理：Jackson 2.17.1。仅适用于Java11及以上版本<br/>microprofile 微Profile<br/>HTTP client: Microprofile client 2.0 (default, set desired version via microprofileRestClientVersion=x.x.x). JSON processing: JSON-B 1.0.2 or Jackson 2.17.1<br/>HTTP客户端：Microprofile客户端2.0（默认值，可通过microprofileRestClientVersion=x.x.x设置所需版本）。JSON处理：JSON-B 1.0.2或Jackson 2.17.1<br/>apache-httpclient<br/>HTTP client: Apache httpclient 5.2.1. JSON processing: Jackson 2.17.1<br/>HTTP客户端：Apache httpclient 5.2.1。JSON处理：Jackson 2.17.1 | okhttp-gson                                                  |
| licenseName 许可证名称                                       | The name of the license 许可证名称                           |                                                              | Unlicense 无许可证                                           |
| licenseUrl                                                   | The URL of the license 许可证的URL                           |                                                              | [http://unlicense.org](http://unlicense.org/)                |
| microprofileFramework 微profile框架                          | Framework for microprofile. Possible values "kumuluzee"MicroProfile 的框架。可能的值为“kumuluzee” |                                                              | null                                                         |
| microprofileGlobalExceptionMapper microprofile全局异常映射器 | Should ApiExceptionMapper be annotated with @Provider making it a global exception mapperApiExceptionMapper是否应该用@Provider注解，使其成为全局异常映射器 |                                                              | true 真                                                      |
| microprofileMutiny                                           | Whether to use async types for microprofile (currently only Smallrye Mutiny is supported).是否为微配置文件使用异步类型（目前仅支持Smallrye Mutiny）。 |                                                              | null                                                         |
| microprofileRegisterExceptionMapper microprofile注册异常映射器 | Should generated API Clients be annotated with @RegisterProvider(ApiExceptionMapper.class).生成的API客户端是否应该用@RegisterProvider(ApiExceptionMapper.class)进行注解。 |                                                              | true 真                                                      |
| microprofileRestClientVersion MicroProfile REST客户端版本    | Version of MicroProfile Rest Client API.MicroProfile REST 客户端 API 的版本。 |                                                              | null                                                         |
| modelPackage 模型包                                          | package for generated models 生成模型的包                    |                                                              | org.openapitools.client.model 组织。打开api工具。客户。模型  |
| openApiNullable openApi可空                                  | Enable OpenAPI Jackson Nullable library. Not supported by `microprofile` library.启用OpenAPI Jackson Nullable库。`microprofile`库不支持此功能。 |                                                              | true 真                                                      |
| parcelableModel                                              | Whether to generate models for Android that implement Parcelable with the okhttp-gson library.是否生成适用于Android的模型，这些模型使用okhttp-gson库实现Parcelable。 |                                                              | false 假                                                     |
| parentArtifactId 父母商品ID                                  | parent artifactId in generated pom N.B. parentGroupId, parentArtifactId and parentVersion must all be specified for any of them to take effect生成的pom中的父artifactId 注意：parentGroupId、parentArtifactId和parentVersion必须全部指定，其中任何一个才能生效 |                                                              | null                                                         |
| parentGroupId 父组ID                                         | parent groupId in generated pom N.B. parentGroupId, parentArtifactId and parentVersion must all be specified for any of them to take effect生成的pom中的父groupId 注意：parentGroupId、parentArtifactId和parentVersion必须全部指定，其中任何一个才能生效 |                                                              | null                                                         |
| parentVersion 父母版本                                       | parent version in generated pom N.B. parentGroupId, parentArtifactId and parentVersion must all be specified for any of them to take effect生成的pom中的父版本 注意：parentGroupId、parentArtifactId和parentVersion都必须指定才能生效 |                                                              | null                                                         |
| performBeanValidation 执行Bean验证                           | Perform BeanValidation 执行BeanValidation                    |                                                              | false 假                                                     |
| prependFormOrBodyParameters                                  | Add form or body parameters to the beginning of the parameter list.在参数列表的开头添加表单或主体参数。 |                                                              | false 假                                                     |
| scmConnection SCM连接                                        | SCM connection in generated pom.xml 生成的pom.xml中的SCM连接 |                                                              | scm:git:[git@github.com](mailto:git@github.com):openapitools/openapi-generator.gitgit@github.com |
| scmDeveloperConnection SCM开发者连接                         | SCM developer connection in generated pom.xml生成的pom.xml中的SCM开发者连接 |                                                              | scm:git:[git@github.com](mailto:git@github.com):openapitools/openapi-generator.gitgit@github.com |
| scmUrl                                                       | SCM URL in generated pom.xml 生成的pom.xml中的SCM URL        |                                                              | https://github.com/openapitools/openapi-generator            |
| serializableModel 序列化模型                                 | boolean - toggle "implements Serializable" for generated models布尔值 - 切换生成模型的“实现Serializable” |                                                              | false 假                                                     |
| serializationLibrary 序列化库                                | Serialization library, default depends on value of the option library序列化库，默认值取决于选项“library”的值 | **jsonb**Use JSON-B as serialization library 使用JSON-B作为序列化库**jackson 杰克逊**Use Jackson as serialization library使用Jackson作为序列化库**gson**Use Gson as serialization library 使用Gson作为序列化库 | null                                                         |
| snapshotVersion 快照版本                                     | Uses a SNAPSHOT version. 使用快照版本。                      | **true 真**Use a SnapShot Version 使用快照版本**false 假**Use a Release Version 使用发布版本 | null                                                         |
| sortModelPropertiesByRequiredFlag                            | Sort model properties to place required parameters before optional parameters.对模型属性进行排序，将必填参数放在可选参数之前。 |                                                              | true 真                                                      |
| sortParamsByRequiredFlag 按必填标志对参数排序                | Sort method arguments to place required parameters before optional parameters.按所需参数在前、可选参数在后的顺序对方法参数进行排序。 |                                                              | true 真                                                      |
| sourceFolder 源文件夹                                        | source folder for generated code 生成代码的源文件夹          |                                                              | src/main/java                                                |
| supportStreaming 支持流式传输                                | Support streaming endpoint (beta) 支持流式端点（测试版）     |                                                              | false 假                                                     |
| supportUrlQuery 支持Url查询字符串生成                        | Generate toUrlQueryString in POJO (default to true). Available on `native`, `apache-httpclient` libraries.在POJO中生成toUrlQueryString（默认为true）。适用于`native`、`apache-httpclient`库。 |                                                              | false 假                                                     |
| supportVertxFuture 支持VertxFuture                           | Also generate api methods that return a vertx Future instead of taking a callback. Only `vertx` supports this option. Requires vertx 4 or greater.同时生成返回vertx Future而非接受回调的API方法。只有`vertx`支持此选项。需要vertx 4或更高版本。 |                                                              | false 假                                                     |
| testOutput 测试输出                                          | Set output folder for models and APIs tests设置模型和API测试的输出文件夹 |                                                              | ${project.build.directory}/generated-test-sources/openapi    |
| useAbstractionForFiles 使用文件抽象                          | Use alternative types instead of java.io.File to allow passing bytes without a file on disk. Available on resttemplate, webclient, restclient, libraries使用替代类型而非java.io.File，以允许在没有磁盘文件的情况下传递字节。在resttemplate、webclient、restclient及相关库中可用。 |                                                              | false 假                                                     |
| useBeanValidation 使用BeanValidation API注解                 | Use BeanValidation API annotations 使用BeanValidation API注解 |                                                              | false 假                                                     |
| useEnumCaseInsensitive 使用不区分大小写的枚举                | Use `equalsIgnoreCase` when String for enum comparison对枚举进行字符串比较时使用`equalsIgnoreCase` |                                                              | false 假                                                     |
| useGzipFeature 使用Gzip功能                                  | Send gzip-encoded requests 发送gzip编码的请求                |                                                              | false 假                                                     |
| useJakartaEe Usejakarae                                      | whether to use Jakarta EE namespace instead of javax是否使用Jakarta EE命名空间而非javax |                                                              | false 假                                                     |
| useOneOfDiscriminatorLookup 使用oneOf鉴别器查找              | Use the discriminator's mapping in oneOf to speed up the model lookup. IMPORTANT: Validation (e.g. one and only one match in oneOf's schemas) will be skipped. Only jersey2, jersey3, native, okhttp-gson support this option.使用鉴别器在oneOf中的映射来加快模型查找速度。重要提示：验证（例如oneOf的模式中仅有一个匹配项）将被跳过。只有jersey2、jersey3、native、okhttp-gson支持此选项。 |                                                              | false 假                                                     |
| useOneOfInterfaces 是否使用Java接口来描述一组oneOf选项，其中每个选项都是实现该接口的类 | whether to use a java interface to describe a set of oneOf options, where each option is a class that implements the interface是否使用Java接口来描述一组“oneOf”选项，其中每个选项都是一个实现该接口的类 |                                                              | false 假                                                     |
| usePlayWS 使用PlayWS                                         | Use Play! Async HTTP client (Play WS API)使用Play!异步HTTP客户端（Play WS API） |                                                              | false 假                                                     |
| useReflectionEqualsHashCode 使用反射实现equals和hashCode     | Use org.apache.commons.lang3.builder for equals and hashCode in the models. WARNING: This will fail under a security manager, unless the appropriate permissions are set up correctly and also there's potential performance impact.在模型中使用org.apache.commons.lang3.builder来实现equals和hashCode方法。警告：在安全管理器下这会失败，除非正确设置了适当的权限，而且这还可能会对性能产生影响。 |                                                              | false 假                                                     |
| useRuntimeException 使用运行时异常                           | Use RuntimeException instead of Exception. Only jersey2, jersey3, okhttp-gson, vertx, microprofile support this option.使用RuntimeException而非Exception。只有jersey2、jersey3、okhttp-gson、vertx、microprofile支持此选项。 |                                                              | false 假                                                     |
| useRxJava2                                                   | Whether to use the RxJava2 adapter with the retrofit2 library. IMPORTANT: This option has been deprecated.是否将RxJava2适配器与retrofit2库一起使用。重要提示：此选项已被弃用。 |                                                              | false 假                                                     |
| useRxJava3                                                   | Whether to use the RxJava3 adapter with the retrofit2 library. IMPORTANT: This option has been deprecated.是否将RxJava3适配器与retrofit2库一起使用。重要提示：此选项已被弃用。 |                                                              | false 假                                                     |
| useSealedOneOfInterfaces 使用密封的OneOf接口                 | Generate the oneOf interfaces as sealed interfaces. Only supported for WebClient and RestClient.将oneOf接口生成为密封接口。仅支持WebClient和RestClient。 |                                                              | false 假                                                     |
| useSingleRequestParameter 使用单个请求参数                   | Setting this property to "true" will generate functions with a single argument containing all API endpoint parameters instead of one argument per parameter. ONLY native, jersey2, jersey3, okhttp-gson, microprofile, Spring RestClient, Spring WebClient support this option. Setting this property to "static" does the same as "true", but also makes the generated arguments class static with single parameter instantiation.将此属性设置为“true”将生成仅包含一个参数的函数，该参数包含所有API端点参数，而非每个参数对应一个参数。只有原生、jersey2、jersey3、okhttp-gson、microprofile、Spring RestClient、Spring WebClient支持此选项。将此属性设置为“static”的效果与“true”相同，但还会使生成的参数类成为静态类，并支持单参数实例化。 |                                                              | false 假                                                     |
| webclientBlockingOperations webclient阻塞操作                | Making all WebClient operations blocking(sync). Note that if on operation 'x-webclient-blocking: false' then such operation won't be sync使所有WebClient操作变为阻塞（同步）状态。请注意，如果操作上设置了“x-webclient-blocking: false”，则此类操作不会是同步的。 |                                                              | false 假                                                     |
| withAWSV4Signature 包含AWS V4签名支持                        | whether to include AWS v4 signature support (only available for okhttp-gson library)是否包含AWS v4签名支持（仅适用于okhttp-gson库） |                                                              | false 假                                                     |
| withXml 包含XML支持                                          | whether to include support for application/xml content type and include XML annotations in the model (works with libraries that provide support for JSON and XML)是否包含对application/xml内容类型的支持，以及是否在模型中包含XML注释（适用于提供JSON和XML支持的库） |                                                              | false 假                                                     |
