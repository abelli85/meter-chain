package org.fisco.bcos.model

/**
 * 报告验证结果
 */
class ReportVerifyResult {
    /** 委托单 */
    var batch: MeterBatch? = null

    /**
     * 报告文件哈希串, 一般使用PDF文件的 sha256sum.
     * sha256sum doc/HYLWGB-20210128.pdf
     * 02b499fe69a7f8d0e7939f823c622cd7025f31cfad63b3382437e0a8c5b9e2b5  doc/HYLWGB-20210128.pdf
     */
    var reportHash: String? = null

    /**
     * 审核员
     */
    var auditor: String? = null

    /**
     * 审核员的签名
     */
    var auditorSigner: String? = null

    /**
     * 审核员的签名验证结果
     */
    var auditorVerify: String? = null

    /**
     * 发布单位, 一般指 检定中心.
     */
    var publisher: String? = null

    /**
     * 发布单位的签名, 一般指 检定中心的签名.
     */
    var publisherSigner: String? = null

    /**
     * 发布单位的签名验证结果.
     */
    var publisherVerify: String? = null

    /**
     * 仲裁机构, 一般指 城市计量局.
     */
    var arbitrator: String? = null

    /**
     * 仲裁机构的签名, 一般指 城市计量局的签名.
     */
    var arbitratorSigner: String? = null

    /**
     * 仲裁机构的签名验证结果.
     */
    var arbitratorVerify: String? = null
}