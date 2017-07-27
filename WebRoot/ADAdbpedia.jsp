<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html style="height: 100%">
   <head>
       <meta charset="utf-8">
   </head>
   <body style="height: 100%; margin: 0">
   	   <div id="input" style="hegiht: 15%;margin:0 auto;text-align:center;">
   	   		<input type="text" id="myinput"></input>
   	   		<button id="submit" onclick = "hehe()">查询</button>
   	   </div>
       <div id="container" style="height: 100%"></div>
       <script src="js/jquery-3.2.1.js"></script>
       <script src="js/echarts.js"></script>
       <script type="text/javascript" src="js/dataTool.min.js"></script>
       <script type="text/javascript">
       function hehe(){
	       	var dom = document.getElementById("container");
	       	var myChart = echarts.init(dom);
			var app = {};
			option=null;
			option={
				backgroundColor: new echarts.graphic.RadialGradient(0.3, 0.3, 0.8, [{
			        offset: 0,
			        color: '#f7f8fa'
			    }, {
			        offset: 1,
			        color: '#cdd0d5'
			    }]),
				title:{
					text: "Demo",
					subtext: "Designed By ApolloDJ",
					top: "top",
					left: "center"
				},
				legend: [{//727
			        formatter: function(name) {
			            return echarts.format.truncateText(name, 200, '14px Microsoft Yahei', '…');
			        },
			        tooltip: {
			            show: true
			        },
			       // itemGap:200,
			        width:120,
			        selectedMode: 'false',
			        //bottom: 20,
			        left:20,
			        data: []
			    }],
				tooltip:{},
				
				series: [{
					name: "general",
					type: "graph",
					layout: "force",
					//color: "blue",
					force: {
						repulsion:250,
						//gravity: 0  //引力
                		edgeLength: 125
					},
					
					categories:[],
					
					data:[],
					links:[],
					focusNodeAdjacency: true,
					roam: true,
					
					label: {
		                normal: {
		                    show: true,
		                    textStyle: {
		                        fontSize: 18
		                    },
		                }
		            },
					edgeSymbol: ['', 'arrow'],
					edgeLabel: {
	                	normal: {
		                    show: true,
		                    color: 'blue',
		                    textStyle: {
		                        fontSize: 10
		                        
			                },
			             //   position: "end",
		                    formatter: "{c}"
		                }
		            },
		            label: {
						normal: {
							show: true,
							textStyle: {
		                        fontSize: 10,
		                        color: "black"
		                    },
						}
					},
					
					lineStyle: {
						normal: {
							color: "source",
							curveness: 0,
							width: 3,
							type: "solid"
						}
					},
				}],
			};
			
			myChart.showLoading();
			
			var temp1={};
			var temp2={};
			var temp3={};//727
			var res_node=[];
			var res_link=[];
			var res_cat=[];
			var res_legend=[];
			
			var input_str=document.getElementById("myinput").value;
			$.ajax({
				type: "post",
				async: true,
				data:{entity:input_str},
				url:"MongoServlet",
				dataType: "json",
				success: function(result){
					if(result != null && result.length > 0){
						
						temp1={
							//	category:input_str,//727
								name:input_str,
								symbolSize:75,
								itemStyle: {
							        normal: {
							      //      color: 'yellow'
							        }
							    }
						};
						res_node.push(temp1);
						temp3={
							name:input_str
						};
						
						res_cat.push(temp3);
						res_legend.push(input_str);
												
						for(var i=1;i<result.length/* &&i<=12 */;i++){//忽略第一个triple
						
							if(result[i].o==result[i].s)
								continue;
							if(result[i].o.length>25||result[i].s.length>25)
								continue;
							temp1={
								category:result[i].s,//727
								name:result[i].o,
								symbolSize:50,
								//value:result[i].value,
								//symbolSize:Math.sqrt(Math.sqrt(Math.sqrt(result[i].value/maxSize*10000)*25)*144),
								draggable:true,
								itemStyle: {
							        normal: {
							       //     color: 'yellow'
							        }
							    }
							};
							
							temp2={
								source:result[i].s,
								target:result[i].o,
								value: result[i].p
							};
							
							if(result[i].p==="ment2ent"){//727
							
								temp1.category=result[i].o;
								temp1.symbolSize=75;
								res_legend.push(result[i].o);
								
								temp3={
									name:result[i].o
								};
							}
							
							res_node.push(temp1);
							res_link.push(temp2);
							res_cat.push(temp3);
						}
						myChart.hideLoading();
						myChart.setOption({
						
							series:[{
								links:res_link,
								data:res_node,
								categories:res_cat
								//roam: true
								}
							],
							
							legend:[{//727
								data:res_legend
							}]
							
						});
					}
					else
						alert("图标数据为空"+result);
						myChart.hideLoading();
				},
				
				error: function(errorMsg){
					alert("图表请求数据失败");
					myChart.hideLoading();	
				}
		
			});
			
			myChart.setOption(option);
		}
       </script>
   </body>
</html>
