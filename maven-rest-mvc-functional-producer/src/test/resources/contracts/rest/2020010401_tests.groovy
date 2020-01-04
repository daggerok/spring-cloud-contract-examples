package contracts.rest // will point to com.github...RestBase.class, but
// package contracts.messaging will point to com.github...MessagingBase.class

import org.springframework.cloud.contract.spec.Contract

[
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
        },

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
        },

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
        },

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
        },

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
        },

        Contract.make {
            priority 1
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
        },
]
