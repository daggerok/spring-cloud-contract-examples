package contracts.rest

import org.springframework.cloud.contract.spec.Contract
import org.springframework.cloud.contract.spec.internal.MatchingTypeValue

Contract.make {
    priority 2020010402
    description '2020010402 - should accept beer request if customer age is greater or equals 21'
    request {
        url('/beer')
        method(POST())
        headers {
            contentType(applicationJson())
        }
        body([
                name: 'Maksimko',
                age: 36,
        ])
        bodyMatchers {
            jsonPath('$.name', byRegex(onlyAlphaUnicode()))
            jsonPath('$.age', byRegex('[2-9][0-9][0-9]?'))
        }
    }
    response {
        status(ACCEPTED())
        headers {
            contentType applicationJson()
        }
        body([
                ACCEPTED: 'Here you are',
        ])
    }
}
