package org.org.numb.openapi.generator.gen.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * SomeObj
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-12-25T08:13:05.193599400+08:00[Asia/Shanghai]", comments = "Generator version: 7.14.0")
public class SomeObj {

  /**
   * Gets or Sets $type
   */
  public enum TypeEnum {
    SOME_OBJ_IDENTIFIER("SomeObjIdentifier");

    private final String value;

    TypeEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static TypeEnum fromValue(String value) {
      for (TypeEnum b : TypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private TypeEnum $type = TypeEnum.SOME_OBJ_IDENTIFIER;

  private @Nullable Long id;

  private @Nullable String name;

  private @Nullable Boolean active;

  private @Nullable String type;

  public SomeObj $type(TypeEnum $type) {
    this.$type = $type;
    return this;
  }

  /**
   * Get $type
   * @return $type
   */
  
  @Schema(name = "$_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("$_type")
  public TypeEnum get$Type() {
    return $type;
  }

  public void set$Type(TypeEnum $type) {
    this.$type = $type;
  }

  public SomeObj id(@Nullable Long id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   */
  
  @Schema(name = "id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("id")
  public @Nullable Long getId() {
    return id;
  }

  public void setId(@Nullable Long id) {
    this.id = id;
  }

  public SomeObj name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public SomeObj active(@Nullable Boolean active) {
    this.active = active;
    return this;
  }

  /**
   * Get active
   * @return active
   */
  
  @Schema(name = "active", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("active")
  public @Nullable Boolean getActive() {
    return active;
  }

  public void setActive(@Nullable Boolean active) {
    this.active = active;
  }

  public SomeObj type(@Nullable String type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  
  @Schema(name = "type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public @Nullable String getType() {
    return type;
  }

  public void setType(@Nullable String type) {
    this.type = type;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SomeObj someObj = (SomeObj) o;
    return Objects.equals(this.$type, someObj.$type) &&
        Objects.equals(this.id, someObj.id) &&
        Objects.equals(this.name, someObj.name) &&
        Objects.equals(this.active, someObj.active) &&
        Objects.equals(this.type, someObj.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash($type, id, name, active, type);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SomeObj {\n");
    sb.append("    $type: ").append(toIndentedString($type)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    active: ").append(toIndentedString(active)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

