package contracts.rest

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    priority 2020010401
    description '2020010401 - should return empty statistics'
    request {
        url '/statistics'
        method GET()
    }
    response {
        status OK()
        headers {
            contentType(applicationJson())
        }
        body([
                "ACCEPTED": 0,
                "REJECTED": value(anyNumber()),
        ])
    }
}