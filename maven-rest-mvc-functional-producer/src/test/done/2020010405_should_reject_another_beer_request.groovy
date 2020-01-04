package contracts.rest

import org.springframework.cloud.contract.spec.Contract
import org.springframework.cloud.contract.spec.internal.MatchingTypeValue

Contract.make {
    priority 1
    description '2020010405 - should reject another beer request if customer age is less then 21'
    request {
        url('/beer')
        method(POST())
        headers {
            contentType(applicationJson())
        }
        body([
                name: anyNonBlankString(),
                age: regex('(([01]?[0-9])|(20))').asInteger(),
        ])
        bodyMatchers {
            jsonPath('$.name', byRegex(nonBlank()))
            jsonPath('$.age', byRegex('(([01]?[0-9])|(20))').asInteger())
        }
    }
    response {
        status(ACCEPTED())
        headers {
            contentType applicationJson()
        }
        body([
                'REJECTED': value(anyNonBlankString()),
        ])
    }
}
