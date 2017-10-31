package config.api

import com.fasterxml.jackson.annotation.JsonIgnore

data class Model(var body: Any, @JsonIgnore var status: Int)
