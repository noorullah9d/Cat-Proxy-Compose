package com.galixo.proxy.compose.data.mapper

import com.galixo.proxy.compose.data.dto.ServerDto
import com.galixo.proxy.compose.data.local.entities.ServerEntity
import com.galixo.proxy.compose.domain.model.ServerModel

fun ServerDto.toServerEntity(): ServerEntity {
    return ServerEntity(
        id = this.id ?: 0,
        ipaddress = this.ipaddress ?: "",
        password = this.password ?: "",
        countryName = this.countryName ?: "",
        cityName = this.cityName ?: "",
        type = this.type ?: "",
        flag = this.flag ?: "",
        protocol = this.protocol ?: "",
        port = this.port ?: "",
        encryption = this.encryption ?: ""
    )
}
fun ServerEntity.toServerModel(): ServerModel {
    return ServerModel(
        id = id,
        ipaddress = ipaddress,
        password = password,
        countryName = countryName,
        cityName = cityName,
        type = type,
        flag = flag,
        protocol = protocol,
        port = port,
        encryption = encryption
    )
}