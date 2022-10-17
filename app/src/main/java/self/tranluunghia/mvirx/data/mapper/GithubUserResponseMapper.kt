package self.tranluunghia.mvirx.data.mapper

import self.tranluunghia.mvirx.core.basemvi.Mapper
import self.tranluunghia.mvirx.data.model.response.GithubUserResponse
import self.tranluunghia.mvirx.domain.model.GithubUser
import javax.inject.Inject

class GithubUserResponseMapper @Inject constructor() : Mapper<GithubUserResponse, GithubUser> {
    override fun map(from: GithubUserResponse) = GithubUser(
        role = from.role,
        isActive = from.isActive,
        dialCode = from.dialCode,
        sex = from.sex,
        name = from.name,
        createdAt = from.createdAt,
        id = from.id,
        type = from.type,
        email = from.email,
        phone = from.phone,
        picture = from.picture,
        dob = from.dob,
        totalFollower = from.totalFollower,
        totalFollowing = from.totalFollowing,
        isFollow = from.isFollow
    )
}