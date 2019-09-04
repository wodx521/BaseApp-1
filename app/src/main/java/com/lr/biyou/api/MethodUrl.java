package com.lr.biyou.api;

/**
 */
public class MethodUrl {




    public static final String REFRESH_TOKEN = "token/refresh";//获取刷新的token
    public static final String tempToken        = "token";//获取临时Token
    public static final String imageCode        = "imgcode";//图形验证码
    public static final String headPath         = "user/uploadheadpic";//上传文件
    public static final String appVersion = "sys/appVersion";//更新app信息
    public static final String shareUrl = "user/codeurl";//注册分享的url

    public static final String nameCode = "code/all";//获取应用全局字典信息
    public static final String idCardCheck = "verify/ocridcard";//身份证验证
    public static final String idCardSubmit = "verify/idcard";//身份证验证提交
    public static final String liveSubmit = "verify/living";//人脸识别提交
    public static final String userAuthInfo = "verify/authinfo";//用户认证信息
    public static final String laseAuthInfo = "verify/man";// 最近一次认证信息
    public static final String peopleAuth = "verify/man";// 人工认证         //方法名一样，但是提交方式不一样

    public static final String companyCheck = "verify/companyLegal";//企业认证信息提交
    public static final String companyInfo = "verify/companyInfo";//企业详细信息查询
    public static final String companyPay = "verify/companyPay";//企业打款申请
    public static final String companyPayVerify = "verify/companyPayVerify";//企业打款验证

    public static final String resetPassCode = "sms/pwd";// 重置密码获取短信验证码
    public static final String modifyLoginPass = "secure/modifyPwd";// 修改登录密码
    public static final String checkCode = "sms";// 验证短信验证码

    public static final String clearCache = "sys/cache_clean";// 清除缓存

    public static final String isTradePass = "secure/trdpwd/state";// 是否设置交易密码
    public static final String checkTradePass = "secure/trdpwd/check";// 检测交易旧密码是否正确
    public static final String setTradePass = "secure/trdpwd";// 设置交易密码
    public static final String forgetTradePass = "sms/forgettrdpwd";// 交易密码忘记 获取短信验证码、



    public static final String isInstallCer = "secure/ca";// 是否安装证书
    public static final String qzImage      = "secure/viewdzyz";// 查看电子签章图片


    public static final String bankCardSms = "acct/card/fast/smsCode";// 获取绑定充值卡短信验证码
    public static final String checkBankSms = "acct/card/fast/bind";// 绑定充值卡验证短信验证码  ---------------
    public static final String supervisionConfig = "acct/supervisionConfig";// 查询资金托管配置  ---------------


    public static final String changePhoneMsgCode = "sms/modtel";// 更换手机号获取短信验证码
    public static final String changePhoneSubmit = "secure/logintel";// 更换新手机号提交信息

    public static final String installCode= "sms/ca";// 安装证书 所需要的验证码操作
    public static final String normalSms = "sms/common";// 通用验证码
    public static final String installCerSubmit = "secure/ca";// 安装证书进行的操作
    public static final String caConfig = "secure/ca/config";// 企业证书信息
    public static final String checkCa = "secure/ca/matchingPay";// 证书是否已支付money  匹配来账信息
    public static final String zsMoneyInfo = "secure/ca/keyCharge";// 转账信息


    public static final String userTelephones = "user/telephones";// 查询新老手机号
    public static final String setNewTel = "user/chgRepeatTel";// 设置新的手机号

    public static final String userMoreInfo = "user/completeInfo";// 查看更多资料信息
    public static final String submitUserInfo = "user/completeInfo";// 完善用户信息
    public static final String minZuList = "code/40000002";// 民族列表
    public static final String zhiyeList = "code/40000001";// 职业列表

    public static final String bankCardList = "acct/card";// 银行卡列表
    public static final String bankCardList2 = "acct/card/chanageList";// 银行卡列表-可变更提现  个人的情况下
    public static final String bankCardLisGroup = "acct/card/group";// 银行卡列表-分组
    public static final String checkBankCard = "acct/card/openBank";//  根据银行卡卡号获取所属银行
    public static final String bankFourCheck = "verify/bankCard";//  银行卡四要素验证
    public static final String bankCard = "acct/card";// 提现账户维护
    public static final String bindCard = "acct/card/withdraw/bind";// 提现账户维护
    public static final String  balanceAccount="acct/balance"; //账户余额
    public static final String tradeList = "acct/trade";// 交易记录列表
    public static final String borrowList = "loan/list";// 借款记录
    public static final String borrowDetail = "loan/detail";// 借款详细
    public static final String payTheInfo = "loan/trustee";// 借款详细
    public static final String repaymentList = "repay/list";// 还款记录
    public static final String repayPlan = "repay/plan";// 还款计划
    public static final String repaymentDetail= "repay/detail";// 还款详情
    public static final String jiekuanSxList = "credit/list";// 授信列表

    public static final String billinfolist = "loan/billinfolist";// 预付款合同列表
    public static final String addbillHt = "loan/addbill";// 预付款合同新增



    public static final String payCompanyList = "loan/payfirm";// 查询付款方
    public static final String yszkList = "loan/yszklist";// 查询应收账款信息
    public static final String hetongInfo = "loan/tradecont";// 查询贸易合同信息
    public static final String addHetongInfo = "loan/completecont";// 添加完善贸易合同信息
    public static final String ruchiAction = "loan/inpoolapply";// 应收账款入池
    public static final String childfirm = "loan/childfirm";// 查询分子公司信息
    public static final String totleMoney = "credit/total";// 总额度
    public static final String shouxinDetail = "credit/detail";// 授信详情
    public static final String hetongList = "credit/contract";// 合同列表  授信合同
    public static final String peopleList = "user/union";// 共同借款人列表
    public static final String addPeople = "credit/unionLoaner";// 添加共同借款人
    public static final String allZichan = "acct/balance";// 总资产  余额查询
    public static final String zhanghuList = "acct";// 账户列表
    public static final String creConfig = "credit/config";// 查询授信申请配置
    public static final String creUploadFile = "upload";// 文件上传
    public static final String applySubmit = "credit/apply";// 申请额度最后提交操作
    public static final String opnbnk= "acct/card/opnbnkList";// 开户行  比如建设银行，不是开户网点
    public static final String erleiHuList = "NCBCard/list";// 二类户查询
    public static final String erleiMoney = "NCBCard/balance";// 二类户余额查询
    public static final String bindList = "NCBCard/bindList";// 绑定一类户信息
    public static final String erleiHuBind = "NCBCard/bind";// 二类户绑定
    public static final String serverRandom = "NCBCard/serverRandom";// 二类户 ---  服务器端随机数字
    public static final String erLeihuPass = "NCBCard/encry";// 二类户 ---  设置密码
    public static final String erLeihuPassOpen = "NCBCard/open";// 二类户 ---  开户 设置密码
    public static final String erLeihuQianYue = "NCBCard/sign";// 网银签约


    public static final String chongzhiMoneyCodeCheck = "acct/recharge/verify";// 充值钱短信验证码验证  ---------------
    public static final String chongzhiSubmit = "acct/recharge";// 充值钱短信验证码验证  ---------------
    public static final String tixianSubmit = "acct/withdraw";// 提现  ---------------
    public static final String unbindCard = "acct/card/delete";// 解绑银行卡  提现  充值  都可以解绑  ---------------


    public static final String workList = "notice/main";// 待办事项列表
    public static final String prePeopleCheck = "credit/preAuditDetail";// 查询预授信_共同借款人
    public static final String peopleCheckSure = "credit/preAudit";// 预授信审核-共同借款人
    public static final String reqCheck = "credit/reqValid";// 授权申请校验
    public static final String reqShouxinDetail = "credit/preDetail";// 预授信详情

    public static final String daihouDetail = "afterLoan/files";// 贷后详情
    public static final String daihouFujianSubmit = "afterLoan/files";// 贷后上传附件修改


    public static final String signDetail = "credit/signDetail";// 授信签署详情
    public static final String signSubmit = "credit/sign";// 授信签署提交操作

    public static final String repayConfig = "repay/config";// 还款申请配置
    public static final String repayCreate = "repay/contract";// 生成还款申请书
    public static final String repayLixi = "repay/interest";// 利息计算
    public static final String repayApply = "repay/apply";// 还款申请
    public static final String pdfUrl = "viewer/pdf";//  （协议）网页链接（pdf文件的查看）


    public static final String skList = "loan/payeeList";// 收款人列表
    public static final String skPeopleAdd = "loan/addPayee";// 添加收款人信息
    public static final String jiekuanConfig = "loan/config";// 借款申请配置
    public static final String xxFuwuFei = "loan/ratecalc";// 借款 服务费
    public static final String bankWdList = "acct/card/opnbnkwd";// 银行网点列表
    public static final String jiekuanHetong = "loan/createConts";// 借款合同生成
    public static final String jiekuanSubmit = "loan/apply";// 借款最后数据提交


    public static final String shoumoneyLine = "loan/pondinfo";// 应收账款图示信息
    public static final String shoumoneyList = "loan/pondinfo2";// 应收账款列表信息







    public static final String RESET_PASSWORD = "Register/forgotPassword";// 重置密码提交
    public static final String LOGIN_ACTION = "Register/userLogin";//登录
    public static final String REGIST_SMSCODE = "Register/code";//注册短信验证码
    public static final String REGIST_ACTION = "Register/userReg";//注册提交

    public static final String BANNNER_INFO = "Home/index";// 首页公告信息
    public static final String NOTICE_LIST = "Home/noticeList";//公告列表
    public static final String NOTICE_LIST_ITEM = "Home/noticeDetails";//公告列表
    public static final String LIST_INFO = "Home/listInfo";//首页实时数据

    public static final String USER_INFO = "Personal/index";//获取用户基本信息
    public static final String IS_IDENTITY = "Personal/isIdentity";//是否实名认证
    public static final String IDENTITY_ACTIVE = "Personal/identityActive";//实名认证操作
    public static final String PAY_WAY_LIST = "Personal/paymentMethod";//收款方式列表
    public static final String UPLOAD_FILE = "Personal/upload";//上传图片文件
    public static final String UPDATE_BANK = "Personal/bankEdit";// 添加银行卡支付方式
    public static final String UPDATE_ALIPAY = "Personal/alipayEdit";//添加支付宝支付方式
    public static final String UPDATE_WECHAT = "Personal/wechatEdit";//添加微信支付方式
    public static final String CONTACT_US = "Personal/contactUs";//联系我们
    public static final String INVIAT_ATION = "Personal/invitation";//邀请链接
    public static final String EDIT_ACCOUNT = "Personal/editAccount";//修改账户






    public static final String AVIABLE_MONEY = "Contract/userContractInfo";//保证金动态收益信息
    public static final String CHICANG_LIST = "Contract/userDepot";//持仓列表
    public static final String WEITUO_LIST = "Contract/queryContractEntrust";//委托列表
    public static final String CHENGJIAO_LIST = "Contract/closeDepot";//成交列表
    public static final String PING_CANG = "contract_trade/contractSell";//平仓操作
    public static final String PING_CANG_ALL = "contract_trade/contractSellAll";//一键平仓操作
    public static final String CHE_XIAO = "contract_trade/cancelEntrust";//撤销操作
    public static final String PAIR_DEPTH = "Contract/queryPairDepth";//合约价格及合并深度信息
    public static final String CONTRACT_LEVER = "Contract/getContractLever";//获取杠杆信息
    public static final String AREA_ALL = "Contract/areaAll";//交易区
    public static final String COIN_ACCOUNT = "Contract/queryCoinAccount";//用户合约币、USDT资产
    public static final String CONTRACT_TRADE = "contract_trade/index";//交易买卖




    public static final String ENTRUST_LIST = "Coin/queryEntrust";//委托单查询
    public static final String COIN_AREAALL = "Coin/areaAll";//交易区列表
    public static final String AREA_ITEM = "Coin/queryArea";//获取对应交易区币对列表
    public static final String AREA_COIN_ACCOUNT = "Coin/queryCoinAccount";//账户当前交易区交易币可用
    public static final String CANCEL_WEITUO = "coin_trade/revocationOrder";//撤销委托
    public static final String CURRENT_PRICE = "Coin/coinCurrent";//币币查询当前币价格信息
    public static final String GUADAN_TRADE = "coin_trade/index";//买卖挂单操作



    public static final String OTC_TRADE = "Outside/getPair";//法币列表交易区
    public static final String FB_ENTRUSTLIST = "Outside/myEntrust";//法币买卖挂单列表




}