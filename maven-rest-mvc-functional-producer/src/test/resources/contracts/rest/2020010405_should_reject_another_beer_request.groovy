package contracts.rest

import org.springframework.cloud.contract.spec.Contract
import org.springframework.cloud.contract.spec.internal.MatchingTypeValue

Contract.make {
    priority 2020010405
    description '2020010405 - should reject another beer request if customer age is less then 21'
    request {
        url('/beer')
        method(POST())
        headers {
            contentType(applicationJson())
        }
        body([
                name: anyNonBlankString(),
                age: 17,
        ])
        bodyMatchers {
            jsonPath('$.name', byRegex(onlyAlphaUnicode()))
            jsonPath('$.age', byRegex('![2-9][1-9][0-9]?'))
        }
    }
    response {
        status(BAD_REQUEST())
        headers {
            contentType applicationJson()
        }
        body([
                'REJECTED': value(anyNonBlankString()),
        ])
    }
}
