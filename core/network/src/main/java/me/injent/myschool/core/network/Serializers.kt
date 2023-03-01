package me.injent.myschool.core.network

import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.JsonTransformingSerializer
import me.injent.myschool.core.network.model.NetworkEduGroup
import me.injent.myschool.core.network.model.NetworkSchool

object SchoolSerializer : JsonTransformingSerializer<List<NetworkSchool>>
    (ListSerializer(NetworkSchool.serializer()))

object EduGroupSerializer : JsonTransformingSerializer<List<NetworkEduGroup>>
    (ListSerializer(NetworkEduGroup.serializer()))