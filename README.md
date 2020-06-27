![](https://github.com/FISCO-BCOS/FISCO-BCOS/raw/master/docs/images/FISCO_BCOS_Logo.svg?sanitize=true)

English / [中文](doc/README_CN.md)

# Meter Chain Starter
This project is built based on spring-boot-starter for fisco bcos.
---

The water meter must be verified according to the [National Measurement Law] before it is installed in a residential house and must be used within the validity period.
Water meters are batch-tested according to the order form. Each order form contains 1 to 100 water meters.

The items of the verification include: appearance verification, pressure verification, electromechanical conversion verification, and flow point verification. If any one of them fails, the water meter is deemed as unqualified.
In the verification content, the flow point verification process is the most complicated. Taking the DN15 water meter as an example, three common flow points need to be verified, which are:
- Q1-31.25 L/h, error requirement +/-5.0%
- Q2-50 L/h, error requirement +/-2.0%
- Q3-2500 L/h, error requirement +/-2.0%

On the automatic calibration table, the water pump is controlled to maintain a constant flow rate, and the water meter wheel is visually recognized, and the starting and readings under the constant flow rate of Q1/Q2/Q3 are recorded,
And compare with the measured water volume of the pump flowmeter of the test bench, and calculate the error value of each flow point.
If the error value of any flow point exceeds the requirements of the regulations, it is considered as unqualified.

Note: 
- From the common water meter calibers DN15, DN20, DN25, ..., DN150, DN200, DN300 and other residential, industrial and commercial users, their needs
the value and number of the verified flow points are different. The larger the caliber, the more the flow points need to be verified.
- The verification period of water meters below DN40 caliber is generally 6 years, and the verification period of above calibers is 2 years.

The project contain 2 modules:
- The business process of water meter verification chain:
    - Receive orders from the manufacturers, and the orders are put on the chain.
    - Verification of water meters, and the results of verification are on the chain. (in the production, verification results can be configured to chain automatically)
    - Completion of order, contract completion.
    - Water meter users can log in to the water meter blockchain website to check the water meter verification results and the validity period. Request to replace them as soon as or near the validity period.
- The business process of report chain:
    - create report for meter verification result.
    - auditor/publisher/arbitrator sign hash (sha256sum/md5sum/etc) of the report.
    - the user verify the verification report on the chain.

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
Just launch mongodb service with default port, empty password. If your mongodb requires password, change application.yml repectively.
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
$ cd meter-chain-starter
$ ./gradlew clean build
$ ./gradlew test
```

When all test cases run successfully, it means that the blockchain is running normally,and the project is connected to the blockchain through the SDK. You can develop your blockchain application based on the project。

After test cases finished, run the following command, you should see "水表链启动...":

```
$ java -jar build/libs/meter-chain-starter-0.0.1-SNAPSHOT.jar
```

Then open "http://localhost:8080/" with browser, it should display:
![Snapshot of homepage](https://raw.githubusercontent.com/abelli85/meter-chain/master/doc/demo-homepage.png)

**Note: If you run the demo project in IntelliJ IDEA or Eclipse, please use gradle wrapper mode. In addition, please enable `Annotation Processors` in `Settings` for IntelliJ IDEA.**

## Test Case Introduction

The sample project provides test cases for developers to use. The test cases are mainly divided into tests for [Web3j API](https://fisco-bcos-documentation.readthedocs.io/en/latest/docs/sdk/sdk.html#web3j-api), [Precompiled Serveice API](https://fisco-bcos-documentation.readthedocs.io/en/latest/docs/sdk/sdk.html#precompiled-service-api), Solidity contract file(https://github.com/abelli85/meter-chain/blob/master/src/main/resources/contract/UserMeter.sol) to Java contract file(https://github.com/abelli85/meter-chain/blob/master/src/main/kotlin/org/fisco/bcos/temp/UserMeter.kt), deployment and call contract.

### Contract Test

Provide `ContractTest` class to test the whole contract. The sample test for verification result chain is as follows:

```kotlin
    /**
     * 测试水表检定结果的上链、添加检定结果、结单、查询。
     */
    @Test
    fun testUserMeter() {
        // ...

        // 委托单开始上链
        val um = UserMeter.deploy(web3j, credentials,
                StaticGasProvider(GasConstants.GAS_PRICE, GasConstants.GAS_LIMIT),
                batch.manufacturer, batch.batchId, "罗工", "2026-6-30").send()
        lgr.info("UserMeter contract address: {}", um.contractAddress)

        // 检定结果上链
        um.verify(batch.meterList!![0].toByteArray(), LocalDateTime.now().toString(), "PASS").send()
        Thread.sleep(100)
        um.verify(batch.meterList!![1].toByteArray(), LocalDateTime.now().toString(), "PASS").send()
        lgr.info("2 meters verified.")

        // 完成委托单
        um.finish().send()

        // 根据合约地址查询水表检定结果.
            val r1 = contract.getInfo(batch.meterList!![0].toByteArray()).send()
            lgr.info("检定结果 - 表码: {}, 委托单号: {}, 厂家: {}, 检定时间: {}, 检定结果: {}, 检定员: {}.",
                    batch.meterList!![0],
                    r1.value1, r1.value2, r1.value3, r1.value4, r1.value5)
            Assert.assertEquals("PASS", r1.value4)

        // ...
    }
```

The sample test for verification report chain is as follows:

```kotlin
    /**
     * 测试水表检定报告的上链、三方签名、查询。
     */
    @Test
    fun testBuildReport() {
        // ...
        // 报告签名
        // sha256sum doc/HYLWGB-20210128.pdf
        // 02b499fe69a7f8d0e7939f823c622cd7025f31cfad63b3382437e0a8c5b9e2b5  doc/HYLWGB-20210128.pdf
        val rptHash = "02b499fe69a7f8d0e7939f823c622cd7025f31cfad63b3382437e0a8c5b9e2b5"
        kotlin.run {
            val cred = Tools.loadkey(FILE_AUDTIOR)!!
            val data = Sign.getSignInterface().signMessage(rptHash.toByteArray(Charsets.US_ASCII), cred.ecKeyPair)
            val signStr = Tools.signatureDataToString(data)
            um.auditorSign(rptHash, cred.address, signStr).send()
            lgr.info("审核员签名 {}: {}.", rpt.batchId, signStr)
        }

        // ...
            val r1 = contract.getSigner().send()
            lgr.info("检定报告 - 委托单号: {}, 审核员: {}, 审核员签名: {}, 检定中心: {}, 检定中心签名: {}, 仲裁机构: {}, 仲裁机构签名: {}.",
                    r1.value1,
                    r1.value2, r1.value3,
                    r1.value4, r1.value5,
                    r1.value6, r1.value7)

            // 验证签名
            lgr.info("审核员签名 {}: {} == {}.", rpt.batchId, Tools.getPublicKey(FILE_AUDTIOR),
                    Tools.verifySignedMessage(rptHash, r1.value3))
            assertEquals(Tools.getPublicKey(FILE_AUDTIOR),
                    Tools.verifySignedMessage(rptHash, r1.value3))
        // ...
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
