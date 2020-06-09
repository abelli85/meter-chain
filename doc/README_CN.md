![](https://github.com/FISCO-BCOS/FISCO-BCOS/raw/master/docs/images/FISCO_BCOS_Logo.svg?sanitize=true)

[English](../README.md) / 中文

# Meter Chain Starter
该项目是基于fisco bcos 的 spring-boot-starter 项目创建。
---

水表在安装到居民住宅前，按国家计量法须检定合格, 且必须在有效期内使用。
水表按委托单进行批量检定，每个委托单包含1～100只水表。

检定内容包括: 外观检定、压力检定、机电转换检定、流量点检定，其中任何一项不合格，该只水表均被认定为不合格。
在检定内容中，以流量点检定过程最为复杂，以DN15水表为例， 需检定3个常用流量点，分别是:
- Q1 - 31.25 L/h, 误差要求 +/-5.0%
- Q2 - 50 L/h, 误差要求 +/-2.0%
- Q3 - 2500 L/h, 误差要求 +/-2.0%

在自动化检定台上，控制水泵保持恒定流量，通过视觉识别水表字轮，分别记录在Q1/Q2/Q3恒定流量下的起至读数，
并与检定台水泵流量计的计量水量进行比较，计算每个流量点的误差值。
任何一个流量点的误差值超过规程要求，均被认定为不合格.

注： 
- 从常用水表口径 DN15、DN20、DN25、...、DN150、DN200、DN300 等居民、工商业用户的水表，其需要
检定的流量点的数值和个数都是不相同的，口径越大的水表，需要检定的流量点个数越多。
- DN40口径以下水表的检定有效期一般为6年，以上口径的检定有效期为2年.


该项目的业务流程：
- 收单 - 接收来自厂商的委托单, 委托单上链.
- 检定 - 检定水表， 检定结果上链. (在生产系统中，可设定检定台自动上链)
- 结单 - 完成委托单， 合约完成.
- 查询 - 水表用户可以登录水表区块链网站，查询水表检定结果及有效期，接近或超出有效期须尽快申请更换.

## 快速启动

### 前置条件
搭建FISCO BCOS区块链，具体步骤[参考这里](https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/installation.html)。

### 获取源码

```
$ git clone https://github.com/abelli85/meter-chain.git
```

#### 节点证书配置
将节点所在目录`nodes/${ip}/sdk`下的`ca.crt`、`sdk.crt`和`sdk.key`文件拷贝到项目的`src/main/resources`目`录下供SDK使用(FISCO BCOS 2.1以前，证书为`ca.crt`、`node.crt`和`node.key`)。

### 配置 mongodb 数据库
启动 mongodb 服务，默认端口，无口令. 如果 mongodb 需要口令，请修改application.yml文件中的对应配置.
```
docker pull mongo
docker run -d --name nosql -p 27017:27017 mongo
```

### 配置文件设置

spring boot项目的配置文件application.yml如下图所示，其中加了注释的内容根据区块链节点配置做相应修改。
  
```yml
encrypt-type: # 0：普通， 1：国密
 encrypt-type: 0 
 
group-channel-connections-config:
  all-channel-connections:
  - group-id: 1  # 群组ID
    connections-str:
                    - 127.0.0.1:20200  # 节点，listen_ip:channel_listen_port
                    - 127.0.0.1:20201
  - group-id: 2  
    connections-str:
                    - 127.0.0.1:20202  # 节点，listen_ip:channel_listen_port
                    - 127.0.0.1:20203
 
channel-service:
  group-id: 1 # sdk实际连接的群组
  agency-name: fisco # 机构名称

accounts:
  pem-file: 0xcdcce60801c0a2e6bb534322c32ae528b9dec8d2.pem # PEM 格式账户文件
  p12-file: 0x98333491efac02f8ce109b0c499074d47e7779a6.p12 # PKCS12 格式账户文件
  password: 123456 # PKCS12 格式账户密码

spring:
  data:
    mongodb:
      uri: mongodb://localhost/test
```
项目中关于SDK配置的详细说明请[参考这里](https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/sdk/sdk.html#sdk)。

### 运行

编译并运行测试案例，在项目根目录下运行：
```
$ cd meter-chain-starter
$ ./gradlew clean build
$ ./gradlew test
```

当所有测试案例运行成功，则代表区块链运行正常，该项目通过SDK连接区块链正常。开发者可以基于该项目进行具体应用开发。

运行测试用例后，执行如下命令行，可看到 "水表链启动...":

```
$ java -jar build/libs/meter-chain-starter-0.0.1-SNAPSHOT.jar
```
此时打开浏览器访问 "http://localhost:8080/", 可以看到:
![首页截图](https://raw.githubusercontent.com/abelli85/meter-chain/master/doc/demo-homepage.png)

**注：如果在IntelliJ IDEA或Eclipse中运行该demo工程，则使用gradle wrapper模式，此外IntelliJ IDEA需要在设置中开启`Annotation Processors`功能。**

## 测试案例介绍

该示例项目提供的测试案例，供开发者参考使用。测试案例主要分为对[Web3j API](https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/sdk/sdk.html#web3j-api)，[Precompiled Serveice API](https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/sdk/sdk.html#precompiled-service-api)、Solidity合约文件(https://github.com/abelli85/meter-chain/blob/master/src/main/resources/contract/UserMeter.sol) 转Java合约文件(https://github.com/abelli85/meter-chain/blob/master/src/main/kotlin/org/fisco/bcos/temp/UserMeter.kt)、部署和调用合约的测试。

### 合约测试

提供[水表检定]的合约测试。测试用例如下：

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

## 贡献代码
- 我们欢迎并非常感谢您的贡献，请参阅[代码贡献流程](https://mp.weixin.qq.com/s/hEn2rxqnqp0dF6OKH6Ua-A)和[代码规范](./CONTRIBUTING_CN.md)。
- 如项目对您有帮助，欢迎star支持！

## 加入我们的社区

**FISCO BCOS开源社区**是国内活跃的开源社区，社区长期为机构和个人开发者提供各类支持与帮助。已有来自各行业的数千名技术爱好者在研究和使用FISCO BCOS。如您对FISCO BCOS开源技术及应用感兴趣，欢迎加入社区获得更多支持与帮助。

![](https://media.githubusercontent.com/media/FISCO-BCOS/LargeFiles/master/images/QR_image.png)

## 相关链接

- 了解FISCO BCOS项目，请参考[FISCO BCOS文档](https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/introduction.html)。
- 了解Web3SDK项目，请参考[Web3SDK文档](https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/sdk/sdk.html)。
- 了解spring boot，请参考[Spring Boot官网](https://spring.io/guides/gs/spring-boot/)。
