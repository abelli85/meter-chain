pragma solidity ^0.4.24;

/**
 * 水表检定结果的上链、添加检定结果、结单.
 */
contract UserMeter {
    /**
     * 水表的检定结果
     */
    struct MeterInfo {
        /**
         * 表码，每只水表出厂编号唯一, 每位定义如下：
         * 第1-2位为厂商代码（详见附录A）
         * 第3位为水表分类（详见附录A）
         * 第4－5位为水表口径分类（详见附录B）
         * ...
         */
        bytes16 meterId;

        /**
         * 检定时间，ISO8601格式，如：2020-06-01T16:01:02Z (GMT时间，北京时间 2020-6-1 8:01:02)
         * 或包含时区: 2020-06-01T08:01:02+0800 (后面不再+Z)
         */
        string verifyTime;

        /**
         * 检定结果， PASS - 合格; FAIL - 不合格.
         */
        string result;
    }

    /**
     * 本合约包含的全部表码
     */
    MeterInfo[] public meterList;

    /**
     * 委托单号
     */
    string public batchId;

    /**
     * 水表厂家
     */
    string public manufacturer;

    /**
     * 检定员姓名
     */
    string public verifierName;

    /**
     * 对于检定报告文件, 支持哈希串上链. 一般可采用PDF文件的 sha256sum.
     * 如果检定报告不上链, 该字段为空.
     */
    string public reportHash;

    /**
     * 检定有效期. ISO8601格式，如：2020-06-01T16:01:02Z (GMT时间，北京时间 2020-6-1 8:01:02)
     * 或包含时区: 2020-06-01T08:01:02+0800 (后面不再+Z)
     */
    string public validDate;

    /**
     * 检定员
     */
    address public verifier;

    /**
     * 该委托单检定完毕. 完成后不可添加水表的检定结果.
     */
    bool public finished;

    /**
     * 审核员
     */
    address public auditor;

    /**
     * 审核员的签名
     */
    address public auditorSigner;

    /**
     * 发布单位, 一般指 检定中心.
     */
    address public publisher;

    /**
     * 发布单位的签名, 一般指 检定中心的签名.
     */
    address public publisherSigner;

    /**
     * 仲裁机构, 一般指 城市计量局.
     */
    address public arbitrator;

    /**
     * 仲裁机构的签名, 一般指 城市计量局的签名.
     */
    address public arbitratorSigner;

    /** 委托单开始. */
    event batchLaunchedEvent(string _batchId);
    /** 审核员 签名. */
    event auditorSignedEvent(string _batchId, address _auditor);
    /** 发布单位 签名. */
    event publisherSignedEvent(string _batchId, address _publisher);
    /** 仲裁机构 签名. */
    event arbitratorSignedEvent(string _batchId, address _arbitrator);
    /** 委托单结束. */
    event batchFinishedEvent(string _batchId);
    /** 水表不存在. */
    event meterNotFound(string _batchId, bytes16 _meterId);

    /**
     * 每个委托单只检定一个厂商的水表.
     * @param _manufacturer - 水表厂家
     * @param _batchId - 委托单号
     * @param _verifierName - 检定员
     * @param _validDate - 检定日期
     */
    constructor(string _manufacturer, string _batchId, string _verifierName, string _validDate) public {
        verifier = msg.sender;
        manufacturer = _manufacturer;
        batchId = _batchId;
        verifierName = _verifierName;
        validDate = _validDate;

        emit batchLaunchedEvent(batchId);
    }

    /**
     * 检定员提交水表的检定结果.
     * @param _meterId - 表码
     * @param _verifyTime - 检定时间， ISO8601格式，如：2020-06-01T16:01:02Z (GMT时间，北京时间 2020-6-1 8:01:02)
     * 或包含时区: 2020-06-01T08:01:02+0800 (后面不再+Z)
     * @param _result - 检定结果， PASS - 合格; FAIL - 不合格.
     */
    function verify(bytes16 _meterId, string _verifyTime, string _result) public {
        require(verifier == msg.sender, "只有检定员可以提交检定结果");
        require(!finished, "委托单完成后不可添加水表的检定结果");

        meterList.push(MeterInfo({
            meterId : _meterId,
            verifyTime : _verifyTime,
            result : _result
            }));
    }

    /**
     * 审核员 签名
     */
    function auditorSign(string _reportHash, address _auditor, address _auditorSigner) public {
        require(!finished, "委托单完成后不可签名");
        reportHash = _reportHash;
        auditor = _auditor;
        auditorSigner = _auditorSigner;

        emit auditorSignedEvent(batchId, auditor);
    }

    /**
     * 发布单位 签名
     */
    function publisherSign(address _publisher, address _publisherSigner) public {
        require(!finished, "委托单完成后不可签名");
        publisher = _publisher;
        publisherSigner = _publisherSigner;

        emit publisherSignedEvent(batchId, publisher);
    }

    /**
     * 仲裁机构 签名
     */
    function arbitratorSign(address _arbitrator, address _arbitratorSigner) public {
        require(!finished, "委托单完成后不可签名");
        arbitrator = _arbitrator;
        arbitratorSigner = _arbitratorSigner;

        emit arbitratorSignedEvent(batchId, arbitrator);
    }

    /**
     * 完成委托单, 完成后不可添加水表的检定结果.
     */
    function finish() public {
        require(verifier == msg.sender, "只有检定员可以完成委托单");
        require(!finished, "委托单完成后不可再次完成");
        require(meterList.length > 0, "空委托单不可以完成");

        finished = true;

        emit batchFinishedEvent(batchId);
    }

    /**
     * 根据表码获取检定结果
     * @param _meterId - 表码
     * @return _manufacturer - 水表厂家
     * @return _verifyTime - 检定时间， ISO8601格式，如：2020-06-01T16:01:02Z (GMT时间，北京时间 2020-6-1 8:01:02)
     * 或包含时区: 2020-06-01T08:01:02+0800 (后面不再+Z)
     * @return _result - 检定结果， PASS - 合格; FAIL - 不合格.
     * @return _verifier - 检定员
     */
    function getInfo(bytes16 _meterId) public view returns (string _batchId,
        string _manufacturer, string _verifyTime, string _result, address _verifier) {
        bool found = false;
        for (uint i = 0; i < meterList.length; i++) {
            MeterInfo storage mi = meterList[i];
            if (mi.meterId == _meterId) {
                found = true;
                _batchId = batchId;

                _manufacturer = manufacturer;

                _verifyTime = mi.verifyTime;
                _result = mi.result;

                _verifier = verifier;
                break;
            }
        }

        // meter not found in the batch.
        if (!found) {
            emit meterNotFound(batchId, _meterId);
        }
    }

    /**
     * 获取签名信息.
     * @return _auditor - 审核员
     * @return _auditorSigner - 审核员的签名
     * @return _publisher - 发布单位, 一般指 检定中心.
     * @return _publisherSigner - 发布单位的签名, 一般指 检定中心的签名.
     * @return _arbitrator - 仲裁机构, 一般指 城市计量局.
     * @return _arbitratorSigner - 仲裁机构的签名, 一般指 城市计量局的签名.
     */
    function getSigner() public view returns (string _batchId,
        address _auditor, address _auditorSigner,
        address _publisher, address _publisherSigner,
        address _arbitrator, address _arbitratorSigner) {
        _batchId = batchId;

        _auditor = auditor;
        _auditorSigner = auditorSigner;

        _publisher = publisher;
        _publisherSigner = publisherSigner;

        _arbitrator = arbitrator;
        _arbitratorSigner = _arbitratorSigner;
    }
}