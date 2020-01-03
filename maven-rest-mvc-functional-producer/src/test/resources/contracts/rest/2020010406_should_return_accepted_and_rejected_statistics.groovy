package contracts.rest

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    priority 2020010406
    description '2020010406 - should return accepted and rejected statistics'
    request {
        url '/statistics'
        method GET()
    }
    response {
        status(OK())
        headers {
            contentType(applicationJson())
        }
        body([
                'ACCEPTED': 1,
                'REJECTED': 2,
        ])
    }
}
