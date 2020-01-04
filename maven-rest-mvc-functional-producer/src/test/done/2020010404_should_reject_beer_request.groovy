package contracts.rest

import org.springframework.cloud.contract.spec.Contract
import org.springframework.cloud.contract.spec.internal.MatchingTypeValue

Contract.make {
    priority 1
    description '2020010404 - should reject beer request if customer age is less then 21'
    request {
        url('/beer')
        method(POST())
        headers {
            contentType(applicationJson())
        }
        body([
                name: anyNonBlankString(),
                // age: regex('([0-1][0-9]|[2][0])').asInteger(),
                age: regex('(-?[0-9]|1[0-9])|20').asInteger(),
        ])
        bodyMatchers {
            jsonPath('$.name', byRegex(nonBlank()))
            // jsonPath('$.age', byRegex('([0-1][0-9]|[2][0])').asInteger())
            jsonPath('$.age', byRegex('(-?[0-9]|1[0-9])|20').asInteger())
        }
    }
    response {
        status(ACCEPTED())
        headers {
            contentType applicationJson()
        }
        body([
                'REJECTED': 'Just go away',
        ])
    }
}
