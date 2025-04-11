package com.task.teamreboott.common

class ErrorMessage {
    companion object {
        const val NON_EXIST_FEATURE = "존재하지 않는 기능이 있습니다"
        const val NOT_FOUND_COMPANY = "조회한 회사가 존재하지 않습니다"
        const val NOT_FOUND_FEATURE = "조회한 기능이 존재하지 않습니다"
        const val NOT_FOUND_PLAN = "조회한 요금제가 존재하지 않습니다"
        const val NOT_FOUND_ENTITY = "엔티티가 존재하지 않습니다"
        const val COMPANY_IS_NOT_MAPPING_PLAN = "회사에 할당된 요금제가 없습니다"
        const val NOT_INCLUDED_FEATURE = "요금제에 해당 기능이 없습니다"
        const val INSUFFICIENT_CREDIT = "잔여 크레딧이 부족합니다"
        const val EXCEED_UNIT_PER_USE = "단일 요청 글자 수가 초과했습니다"
        const val EXCEED_USAGE_PER_MONTH = "월간 한도를 초과했습니다"
    }
}