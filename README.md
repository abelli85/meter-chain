![](https://github.com/FISCO-BCOS/FISCO-BCOS/raw/master/docs/images/FISCO_BCOS_Logo.svg?sanitize=true)

English / [中文](doc/README_CN.md)

# Meter Chain Starter
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg?style=flat-square)](http://makeapullrequest.com)
[![Build Status](https://travis-ci.org/FISCO-BCOS/spring-boot-starter.svg?branch=master)](https://travis-ci.org/FISCO-BCOS/spring-boot-starter)
[![CodeFactor](https://www.codefactor.io/repository/github/fisco-bcos/spring-boot-starter/badge)](https://www.codefactor.io/repository/github/fisco-bcos/spring-boot-starter)
---

The sample spring boot project is based on [Web3SDK](https://fisco-bcos-documentation.readthedocs.io/en/latest/docs/sdk/sdk.html), which provides the basic framework and basic test cases for blockchain application and helps developers to quickly develop applications based on the FISCO BCOS blockchain. **The version only supports** [FISCO BCOS 2.0+](https://fisco-bcos-documentation.readthedocs.io/en/latest/).

## Quickstart

### Precodition

Build FISCO BCOS blockchain, please check out [here](https://fisco-bcos-documentation.readthedocs.io/en/latest/docs/installation.html)。

### Download

```
$ git clone https://github.com/abelli85/meter-chain.git
```
#### Certificate Configuration
Copy the `ca.crt`, `sdk.crt`, and `sdk.key` files in the node's directory `nodes/${ip}/sdk` to the project's `src/main/resources` directory.(Before FISCO BCOS 2.1, the certificate files are `ca.crt`, `node.crt` and `node.key`)

### Mongodb Required
Just launch mongodb service with default port, empty password.
```
docker pull mongo
docker run -d --name nosql -p 27017:27017 mongo
```

### Settings

The `application.yml` of the spring boot project is shown below, and the commented content is modified according to the blockchain node configuration.
  
```yml
encrypt-type: # 0:standard, 1:guomi
 encrypt-type: 0 
 
group-channel-connections-config:
  all-channel-connections:
  - group-id: 1  # group ID
    connections-str:
                    - 127.0.0.1:20200  # node listen_ip:channel_listen_port
                    - 127.0.0.1:20201
  - group-id: 2  
    connections-str:
                    - 127.0.0.1:20202  # node listen_ip:channel_listen_port
                    - 127.0.0.1:20203
 
channel-service:
  group-id: 1 # The specified group to which the SDK connects
  agency-name: fisco # agency name

accounts:
  pem-file: 0xcdcce60801c0a2e6bb534322c32ae528b9dec8d2.pem # PEM format account file
  p12-file: 0x98333491efac02f8ce109b0c499074d47e7779a6.p12 # PKCS12 format account file
  password: 123456 # PKCS12 format account password

spring:
  data:
    mongodb:
      uri: mongodb://localhost/test
```

A detail description of the SDK configuration for the project, please checkout [ here](https://fisco-bcos-documentation.readthedocs.io/en/latest/docs/sdk/sdk.html#sdk)。

### Run

Compile and run test cases:

```
$ cd spring-boot-starter
$ ./gradlew build
$ ./gradlew test
```

When all test cases run successfully, it means that the blockchain is running normally,and the project is connected to the blockchain through the SDK. You can develop your blockchain application based on the project。

**Note: If you run the demo project in IntelliJ IDEA or Eclipse, please use gradle wrapper mode. In addition, please enable `Annotation Processors` in `Settings` for IntelliJ IDEA.**

## Test Case Introduction

The sample project provides test cases for developers to use. The test cases are mainly divided into tests for [Web3j API](https://fisco-bcos-documentation.readthedocs.io/en/latest/docs/sdk/sdk.html#web3j-api), [Precompiled Serveice API](https://fisco-bcos-documentation.readthedocs.io/en/latest/docs/sdk/sdk.html#precompiled-service-api), Solidity contract file to Java contract file, deployment and call contract.

### Contract Test

Provide `ContractTest` class to test the whole contract. The sample test is as follows:

```kotlin
    /**
     * 测试水表检定结果的上链、添加检定结果、结单、查询。
     */
    @Test
    fun testUserMeter() {
        mongoTemplate!!.save(batch)
        lgr.info("新到检定委托单: {}", JSON.toJSONString(batch, true))

        // 委托单开始上链
        val um = UserMeter.deploy(web3j, credentials,
                StaticGasProvider(GasConstants.GAS_PRICE, GasConstants.GAS_LIMIT),
                "宁波水表").send()
        lgr.info("UserMeter contract address: {}", um.contractAddress)
        batch.apply {
            verifierAddress = um.verifier().send().toString()
            contractAddress = um.contractAddress
        }
        mongoTemplate.updateFirst(
                Query.query(Criteria.where("batchId").`is`(batch.batchId)),
                Update.update("verifierAddress", batch.verifierAddress)
                        .addToSet("contractAddress", batch.contractAddress),
                MeterBatch::class.java)

        // 空委托单不允许完成
        um.finish().send()
        lgr.warn("空委托单不允许完成")

        um.verify(batch.meterList!![0].toByteArray(), LocalDateTime.now().toString(), "PASS").send()
        Thread.sleep(100)
        um.verify(batch.meterList!![1].toByteArray(), LocalDateTime.now().toString(), "PASS").send()
        lgr.info("2 meters verified.")

        // 完成委托单
        um.finish().send()
        batch.apply {
            finishDate = Date()
        }
        mongoTemplate.updateFirst(
                Query.query(Criteria.where("batchId").`is`(batch.batchId)),
                Update.update("finishDate", batch.finishDate),
                MeterBatch::class.java
        )
        lgr.info("完成委托单")

        // 立即查询检定结果
        kotlin.run {
            val r1 = um.getInfo(batch.meterList!![0].toByteArray()).send()
            lgr.info("检定结果 - 表码: {}, 厂家: {}, 检定时间: {}, 检定结果: {}, 检定员: {}.",
                    batch.meterList!![0],
                    r1.value1, r1.value2, r1.value3, r1.value4)
            Assert.assertEquals("PASS", r1.value3)

            val r2 = um.getInfo(batch.meterList!![1].toByteArray()).send()
            lgr.info("检定结果 - 表码: {}, 厂家: {}, 检定时间: {}, 检定结果: {}, 检定员: {}.",
                    batch.meterList!![1],
                    r2.value1, r2.value2, r2.value3, r2.value4)
            Assert.assertEquals("PASS", r2.value3)
        }

        // 根据合约地址查询水表检定结果.
        val batchList = mongoTemplate.findAll(MeterBatch::class.java)
        Assert.assertTrue(batchList.size > 0)
        batchList.first().also {
            lgr.info("retrieving contract for the meter-batch: {}", JSON.toJSONString(it, true))

            val contract = UserMeter.load(it.contractAddress, web3j, credentials,
                    StaticGasProvider(
                            GasConstants.GAS_PRICE, GasConstants.GAS_LIMIT))

            val r1 = contract.getInfo(batch.meterList!![0].toByteArray()).send()
            lgr.info("检定结果 - 表码: {}, 厂家: {}, 检定时间: {}, 检定结果: {}, 检定员: {}.",
                    batch.meterList!![0],
                    r1.value1, r1.value2, r1.value3, r1.value4)
            Assert.assertEquals("PASS", r1.value3)

            val r2 = contract.getInfo(batch.meterList!![1].toByteArray()).send()
            lgr.info("检定结果 - 表码: {}, 厂家: {}, 检定时间: {}, 检定结果: {}, 检定员: {}.",
                    batch.meterList!![1],
                    r2.value1, r2.value2, r2.value3, r2.value4)
            Assert.assertEquals("PASS", r2.value3)
        }
    }
```

## Code Contribution

- Your contributions are most welcome and appreciated. Please read the [contribution instructions ](CONTRIBUTING.md).
- If this project is useful to you, please star us on GitHub project page!

## Join Our Community

The FISCO BCOS community is one of the most active open-source blockchain communities in China. It provides long-term technical support for both institutional and individual developers and users of FISCO BCOS. Thousands of technical enthusiasts from numerous industry sectors have joined this community, studying and using FISCO BCOS platform. If you are also interested, you are most welcome to join us for more support and fun.

![](https://media.githubusercontent.com/media/FISCO-BCOS/LargeFiles/master/images/QR_image_en.png)

## Related Links

- For FISCO BCOS project, please check out [FISCO BCOS Documentation](https://fisco-bcos-documentation.readthedocs.io/en/latest/docs/introduction.html)。
- For Web3SDK project, please check out [Web3SDK Documentation](https://fisco-bcos-documentation.readthedocs.io/en/latest/docs/sdk/sdk.html)。
- For Spring Boot applications, please check out [Spring Boot](https://spring.io/guides/gs/spring-boot/)。
