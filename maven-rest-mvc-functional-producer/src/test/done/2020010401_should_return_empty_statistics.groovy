package contracts.rest // will point to com.github...RestBase.class, but
// package contracts.messaging will point to com.github...MessagingBase.class

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    priority 1
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
                "REJECTED": 0,
        ])
    }
}
