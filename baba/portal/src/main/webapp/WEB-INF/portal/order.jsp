<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>订单结算页 -新巴巴商城</title>
<!--结算页面样式-->
<link rel="stylesheet" type="text/css" href="/css/base.css" media="all" />
<script type="text/javascript" src="/js/jquery-1.6.4.js"></script>
<script type="text/javascript" src="/js/base.js"></script>
<link type="text/css" rel="stylesheet" href="/css/a_002.css" source="widget">
<link type="text/css" rel="stylesheet" href="/css/a.css">
</head>

<body id="mainframe">

<jsp:include page="commons/shortcut.jsp" />

<div class="w w1 header clearfix">
    <div id="logo">
        <a href="/"><img src="/images/XBB2.png" alt="新巴巴商城"></a>
        <a href="javascript:;" class="link2"><b></b>"结算页"</a>
    </div>
    <div class="stepflex" id="#sflex03">
        <dl class="first done">
            <dt class="s-num">1</dt>
            <dd class="s-text">
                1.我的购物车
                <s></s><b></b>
            </dd>
        </dl>
        <dl class="normal doing">
            <dt class="s-num">2</dt>
            <dd class="s-text">
                2.填写核对订单信息
                <s></s><b></b>
            </dd>
        </dl>
        <dl class="normal last">
            <dt class="s-num">3</dt>
            <dd class="s-text">
                3.成功提交订单
                <s></s><b></b>
            </dd>
        </dl>
    </div>
</div>

<form action="/buyer/submitOrder" method="post">
	<div id="container">
		<div id="content" class="w">
			<div class="m">
				<div class="checkout-tit">
					<span class="tit-txt">填写并核对订单信息</span>
				</div>
				<div class="mc">
					<div class="checkout-steps">
						<div class="step-tit">
							<h3>收货人信息</h3>
							<div class="extra-r">
								<a href="javascript:;" class="ftx-05">新增收货地址</a>
                                <input id="del_consignee_type" value="0" type="hidden">
							</div>
						</div>
						<div class="step-cont">
							<div id="consignee-addr" class="consignee-content">
								<div class="consignee-scrollbar">
									<div class="ui-scrollbar-main">
										<div class="consignee-scroll">
											<div style="height: 42px;" class="consignee-cont" id="consignee1">
												<ul id="consignee-list">
													<li class="ui-switchable-panel ui-switchable-panel-selected" style="display: list-item;" id="consignee_index_137629184" selected="selected">
														<div class="consignee-item item-selected" id="consignee_index_div_137629184">
															<span limit="8" title="范冰冰">范冰冰 北京</span><b></b>
														</div>
														<div class="addr-detail">
															<span title="范冰冰 " class="addr-name" limit="6">范冰冰</span>
                                                            <span class="addr-info" limit="45">北京 海淀区 西三旗XXXXXXXXX</span>
                                                            <span class="addr-tel">158***888888</span>
														</div>
														<div class="op-btns" consigneeid="137629184">
															<a href="#none" class="ftx-05 setdefault-consignee" fid="137629184">设为默认地址</a>
                                                            <a href="#none" class="ftx-05 edit-consignee" fid="137629184">编辑</a>
                                                            <a href="#none" class="ftx-05 del-consignee" fid="137629184">删除</a>
														</div>
													</li>
												</ul>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div class="hr">

                        </div>
						<div id="shipAndSkuInfo">
							<div id="payShipAndSkuInfo">
								<div class="step-tit">
									<h3>支付方式</h3>
								</div>
								<div class="step-cont">
									<div class="payment-list" >
										<div class="list-cont">
											<ul id="payment-list">
												<input type="hidden" name="paymentWay" value="1">
                                                <li style="cursor: pointer;" onclick="paymentWay(this,1);">
                                                    <div class="payment-item item-selected online-payment" for="pay-method-1" payname="货到付款" payid="1">
                                                        <b></b> 货到付款
                                                    </div>
                                                </li>
                                                <li style="cursor: pointer;" onclick="paymentWay(this,2);">
                                                    <div class="payment-item  online-payment " for="pay-method-2" payname="在线支付" payid="2">
                                                        <b></b> 在线支付
                                                    </div>
                                                </li>
                                                <li style="cursor: pointer;" onclick="paymentWay(this,3); ">
                                                    <div class="payment-item  online-payment " for="pay-method-3" payname="公司转账" payid="3">
                                                        <b></b> 公司转账
                                                    </div>
                                                </li>
                                                <li style="cursor: pointer;" onclick="paymentWay(this,4); ">
                                                    <div class="payment-item  online-payment" for="pay-method-4" payname="邮局汇款" payid="4">
                                                        <b></b> 邮局汇款
                                                    </div>
                                                </li>
											</ul>
										</div>
									</div>
								</div>
								<div class="hr"></div>
								<div class="step-tit">
									<h3>送货清单</h3>
									<div class="extra-r">
										<a href="/cart" class="return-edit ftx-05">返回修改购物车</a>
									</div>
								</div>

								<div class="step-cont" id="skuPayAndShipment-cont">
									<div class="shopping-lists" id="shopping-lists">
										<div class="shopping-list ABTest">
											<div class="goods-list">

												<div class="goods-tit">
													<h4 class="vendor_name_h" id="0">商家：新巴巴自营</h4>
												</div>

												<div class="goods-items">
													<div class="goods-suit goods-last">
														<div class="goods-suit-tit">
															<span class="sales-icon">满赠</span>
                                                            <strong>已购满2.00元 ，再加4.00元，可返回购物车领取赠品 </strong>
														</div>
                                                        <c:forEach items="${cart.items}" var="item">
                                                            <div class="goods-item goods-item-extra" goods-id="${item.skuId}">
                                                                <div class="p-img">
                                                                    <a target="_blank" href="#">
                                                                        <img src="${item.sku.imgUrl}" alt="${item.sku.productName}">
                                                                    </a>
                                                                </div>
                                                                <div class="goods-msg">
                                                                    <div class="goods-msg-gel">
                                                                        <div class="p-name">
                                                                            <a href="#" target="_blank">${item.sku.productName}</a>
                                                                        </div>
                                                                        <div class="p-price">
                                                                            <strong class="jd-price">${item.sku.skuPrice}</strong>
                                                                            <span class="p-num"> x &nbsp; ${item.amount } = ${item.sku.skuPrice * item.amount}</span>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                                <div>
                                                                    <i class="p-icon p-icon-w"></i><span class="ftx-04">7天无理由退货</span>
                                                                </div>
                                                                <div class="clr"></div>
                                                            </div>
                                                        </c:forEach>
													</div>
												</div>
											</div>
											<div class="dis-modes">
												<div class="mode-item mode-tab">
													<div class="mode-item-tit">
														<h4>配送方式</h4>
														<div class="extral-r">
															<a id="jd-goods-item" class="cor-goods" href="#none"><i></i>对应商品</a>
														</div>
													</div>
													<div class="mode-tab-nav">
														<ul>
															<li class="mode-tab-item curr">
																<span id="jdShip-span-tip" class="m-txt">新巴巴快递</span>
                                                            </li>
														</ul>
													</div>
													<div class="mode-tab-con  ui-switchable-panel-selected" id="jd_shipment">
														<ul class="mode-list">
															<li>
																<div class="fore1" id="payment_name_div">
																	<span class="ftx-03">付款方式：</span>
                                                                    <input type="radio" value="1" name="paymentCash" checked="checked" />现金
                                                                    <input type="radio" value="2" name="paymentCash" />POS机
																</div>
															</li>
															<li>
																<div class="fore1" id="jd_shipment_calendar_date">
																	<span class="ftx-03">配送时间：</span>
                                                                    预计&nbsp;1月12日&nbsp;09:00-15:00&nbsp;送达
																</div>
																<div class="fore2">
																	<a href="javascript:;" class="ftx-05">修改</a>
																</div>
															</li>
														</ul>
													</div>
												</div>
											</div>
											<div class="clr"></div>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div style="display: block;" class="order-remarks hide" id="orderRemarkItem">
							<div class="remark-tit">添加订单备注</div>
							<div id="remarkId" style="margin-bottom: 7px">
								<div class="form remark-cont">
									<input name="note" maxlength="45" size="15" class="itxt itxt01" placeholder="限45个字（定制类商品，请将购买需求在备注中做详细说明）" onblur="if(this.value==''||this.value=='限45个字（定制类商品，请将购买需求在备注中做详细说明）'){this.value='限45个字（定制类商品，请将购买需求在备注中做详细说明）';this.style.color='#cccccc'}" onfocus="if(this.value=='限45个字（定制类商品，请将购买需求在备注中做详细说明）') {this.value='';};this.style.color='#000000';" type="text">
                                    <span class="ftx-03 ml10">&nbsp;&nbsp;提示：请勿填写有关支付、收货、发票方面的信息</span>
								</div>
							</div>
						</div>
						<div class="hr"></div>
						<div class="step-tit" id="invoice-step">
							<h3>发票信息</h3>
						</div>
						<div class="step-content">
							<div id="part-inv" class="invoice-cont">
								<span class="mr10"> 普通发票（纸质） &nbsp; </span>
                                <span class="mr10">个人 &nbsp; </span>
                                <span class="mr10"> 明细 &nbsp; </span>
                                <a href="#" class="ftx-05 invoice-edit" onclick="edit_Invoice()">修改</a>
							</div>
						</div>
						<div class="clr"></div>
						<div class="hr"></div>
					</div>
				</div>
			</div>

			<div class="order-summary">
				<div class="statistic fr">
					<div class="list">
						<span>
                            <em class="ftx-01">${cart.productAmount }</em> 件商品，总商品金额：</span>
                            <em class="price" id="warePriceId" v="343.8">￥${cart.productPrice }</em>
					</div>
					<div class="list">
						<span><i class="freight-icon"></i>运费：</span>
                        <em class="price" id="freightPriceId"><font color="#FF6600">￥${cart.fee }</font></em>
					</div>
					<div class="list">
						<span>应付总额：</span>
                        <em class="price" id="sumPayPriceId">￥${cart.productPrice }</em>
					</div>
				</div>
				<div class="clr"></div>
			</div>

			<div class="hr"></div>
			<!-- 配送地址确认 -->
			<div class="trade-foot">
				<div class="consignee-foot">
					<p>寄送至： 北京 海淀区 西三旗 XXXXXXXXXXXXXXXXX</p>
					<p>收货人：范冰冰 158****8888</p>
				</div>
				<div id="checkout-floatbar" class="group">
					<div class="ui-ceilinglamp checkout-buttons">
						<div class="sticky-wrap">
							<div class="inner">
								<input type="submit" class="checkout-submit" value="提交订单" />
								<span class="total">应付总额：<strong id="payPrice">￥${cart.productPrice }</strong></span>
							</div>
						</div>
					</div>
				</div>
			</div>

			<!-- 我要反馈 -->
			<div id="backpanel">
				<div style="right: 139.5px;" id="backpanel-inner" class="hide">
					<div class="bp-item bp-item-survey">
						<a href="javascript:;" class="survey">我要反馈</a>
					</div>
					<div class="bp-item bp-item-backtop" data-top="0">
						<a href="#none" class="backtop" target="_self">返回顶部</a>
					</div>
				</div>
			</div>
		</div>
	</div>
</form>

<script type="text/javascript">
    //选择支付方式
    function paymentWay(target,p){
        $("#payment-list div").removeClass("item-selected");
        $(target).children("div").addClass("item-selected");
        $("input[name='paymentWay']").val(p);
    }
</script>

<jsp:include page="commons/footer.jsp" />

</body>
</html>