$(function(){
	
	var barChart = echarts.init(document.getElementById('barDiv'));
	
	var option = {
		    backgroundColor: '#00265f',
		    angleAxis: {
		        interval: 1,
		        type: 'category',
		        data: ['上海市',
		            '北京市',
		            '深圳市',
		            '河南省',
		            '江苏省',
		            '浙江省',
		            '四川省',
		            '广东省',
		            '湖北省',
		            '湖南省',
		        ],
		        z: 10,
		        axisLine: {
		            show: true,
		            lineStyle: {
		                color: "#00c7ff",
		                width: 1,
		                type: "solid"
		            },
		        },
		        axisLabel: {
		            interval: 0,
		            show: true,
		            color: "#00c7ff",
		            margin: 8,
		            fontSize: 16
		        },
		    },
		    radiusAxis: {
		        min: 0,
		        max: 100,
		        interval: 20,
		        axisLine: {
		            show: true,
		            lineStyle: {
		                color: "#00c7ff",
		                width: 1,
		                type: "solid"
		            },
		        },
		        axisLabel: {
		            formatter: '{value} %',
		            show: true,
		            padding: [0, 0, 20, 0],
		            color: "#00c7ff",
		            fontSize: 16
		        },
		        splitLine: {
		            lineStyle: {
		                color: "#00c7ff",
		                width: 1,
		                type: "solid"
		            }
		        }
		    },
		    polar: {},
		    series: [{
		        type: 'bar',
		        data: [{
		                value: 87,
		                itemStyle: {
		                    normal: {
		                        color: "#f54d4d"
		                    }
		                }
		            },
		            {
		                value: 80,
		                itemStyle: {
		                    normal: {
		                        color: "#f87544"
		                    }
		                }
		            },
		            {
		                value: 75,
		                itemStyle: {
		                    normal: {
		                        color: "#ffae00"
		                    }
		                }
		            },
		            {
		                value: 69,
		                itemStyle: {
		                    normal: {
		                        color: "#dcff00"
		                    }
		                }
		            },
		            {
		                value: 63,
		                itemStyle: {
		                    normal: {
		                        color: "#25d053"
		                    }
		                }
		            },
		            {
		                value: 54,
		                itemStyle: {
		                    normal: {
		                        color: "#01fff5"
		                    }
		                }
		            },
		            {
		                value: 47,
		                itemStyle: {
		                    normal: {
		                        color: "#007cff"
		                    }
		                }
		            },
		            {
		                value: 40,
		                itemStyle: {
		                    normal: {
		                        color: "#4245ff"
		                    }
		                }
		            },
		            {
		                value: 35,
		                itemStyle: {
		                    normal: {
		                        color: "#c32eff"
		                    }
		                }
		            },
		            {
		                value: 33,
		                itemStyle: {
		                    normal: {
		                        color: "#ff62e8"
		                    }
		                }
		            }
		        ],
		        coordinateSystem: 'polar',
		    }],
		};
		
		barChart.setOption(option);
	

});