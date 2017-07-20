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
				
				tooltip:{},
				
				series: [{
					name: "general",
					type: "graph",
					layout: "force",
					//color: "blue",
					force: {
						repulsion:700
					},
					
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
		                    textStyle: {
		                        fontSize: 18
			                },
			             //   position: "end",
		                    formatter: "{c}"
		                }
		            },
		            label: {
						normal: {
							show: true,
							textStyle: {
		                        fontSize: 18,
		                        color: "black"
		                    },
						}
					},
					
					lineStyle: {
						normal: {
							//color: "blue",
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
			var res_node=[];
			var res_link=[];
			var input_str=document.getElementById("myinput").value;
			$.ajax({
				type: "post",
				async: true,
				data:{entity:input_str},
				url:"testServlet",
				dataType: "json",
				success: function(result){
					if(result != null && result.length > 0){
					
						var maxSize=0;
						
						for(var i=0;i<result.length&&i<=12;i++){
							if(maxSize<result[i].value)
								maxSize=result[i].value;
						}
						
					
						temp1={
								category:1,
								name:input_str,
								value:50,
								symbolSize:75,
								itemStyle: {
							        normal: {
							      //      color: 'yellow'
							        }
							    }
						};
						res_node.push(temp1);
						
						
						for(var i=0;i<result.length&&i<=12;i++){
							
							temp1={
								category:1,
								name:result[i].name,
								value:result[i].value,
								symbolSize:Math.sqrt(Math.sqrt(Math.sqrt(result[i].value/maxSize*10000)*25)*144),
								draggable:true,
								itemStyle: {
							        normal: {
							       //     color: 'yellow'
							        }
							    }
							};
							
							temp2={
								source:input_str,
								target:result[i].name,
								value: "是"
							};
							
							res_node.push(temp1);
							res_link.push(temp2);
							
						}
						myChart.hideLoading();
						myChart.setOption({
						
							series:[{
								links:res_link,
								data:res_node,
								//roam: true
								}
							]
							
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