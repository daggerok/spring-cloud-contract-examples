package contracts.rest

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    priority 1
    description '2020010402 - should accept beer request if customer age is greater or equals 21'
    request {
        url('/beer')
        method(POST())
        headers {
            contentType(applicationJson())
        }
        body([
                // name: anyNonBlankString(),
                // age: regex('[2-9][0-9][0-9]?').asInteger(),
                name: 'Maksimko',
                age : 36,
        ])
        bodyMatchers {
            jsonPath('$.name', byRegex(nonBlank()))
            jsonPath('$.age', byRegex('^[2-9][0-9][0-9]?$').asInteger())
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
