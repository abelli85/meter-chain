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
     * 水表厂家
     */
    string public manufacturer;

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
     * 发布单位
     */
    address public publisher;

    /**
     * 每个委托单只检定一个厂商的水表.
     * @param _manufacturer - 水表厂家
     */
    constructor(string _manufacturer) public {
        verifier = msg.sender;
        manufacturer = _manufacturer;
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
            meterId: _meterId,
            verifyTime: _verifyTime,
            result: _result
        }));
    }

    /**
     * 完成委托单, 完成后不可添加水表的检定结果.
     */
    function finish() public {
        require(verifier == msg.sender, "只有检定员可以完成委托单");
        require(!finished, "委托单完成后不可再次完成");
        require(meterList.length > 0, "空委托单不可以完成");

        finished = true;
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
    function getInfo(bytes16 _meterId) public view returns (string _manufacturer, string _verifyTime, string _result, address _verifier) {
        for (uint i = 0; i < meterList.length; i++) {
            MeterInfo storage mi = meterList[i];
            if (mi.meterId == _meterId) {
                _manufacturer = manufacturer;

                _verifyTime = mi.verifyTime;
                _result = mi.result;

                _verifier = verifier;
                break;
            }
        }
    }
}