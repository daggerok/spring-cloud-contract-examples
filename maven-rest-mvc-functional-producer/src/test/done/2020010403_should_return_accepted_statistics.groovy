package contracts.rest

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    priority 1
    description '2020010403 - should return accepted statistics'
    request {
        url '/statistics'
        method GET()
    }
    response {
        status 200
        headers {
            contentType(applicationJson())
        }
        body([
                "ACCEPTED": 1,
                "REJECTED": 0,
                // "REJECTED": anyNumber(),
        ])
    }
}
