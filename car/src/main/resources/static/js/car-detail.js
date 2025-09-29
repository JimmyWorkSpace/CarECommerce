new Vue({
    el: '#app',
    data: {
        swiper: null,
        allSwaggerImages: [],
        currentMediaIndex: 0,
        activeTab: {},
        showVideo: false,
        carImages: [],
        carVideo: [],
        carInfo: {
            dealer: {}
        },
        carId: '',
        carEquipments: [],
        carGuarantees: [],
        tabs: [
            { title: '車輛配備', code: 'car_equipments' },
            { title: '車商介紹', code: 'dealer_intro' },
        ],
        visibleLightbox: false,
        lightboxIndex: 0,
        phoneButtonHover: false,
        lineButtonHover: false,
        lineIdHover: false,
        imageViewerVisible: false,
        currentImageViewerIndex: 0,
        imageOnlyList: []
    },
    computed: {
        currentImage() {
            return this.allSwaggerImages[this.currentMediaIndex]
        },
        lightboxImages() {
            return this.allSwaggerImages
                .filter(media => media.type === 'image')
                .map(media => media.url);
        }
    },
    async created() {
        this.activeTab = this.tabs[0];

        this.$watch('activeTab', (newTab) => {
            if (newTab.code === 'car_desc') {
                this.$nextTick(() => {
                    this.resizeContentFrame('contentFrame');
                });
            } else if (newTab.code === 'dealer_intro') {
                this.$nextTick(() => {
                    this.resizeContentFrame('dealerContentFrame');
                });
            }
        });

        this.$nextTick(() => {
            this.setupAllContentFrames();
        });

        window.addEventListener('message', this.handleIframeMessage);

        this.carId = carId;
        // 初始化数据
        this.fetchCarBaseInfo(carId);
        this.fetchCarEquipments(carId);
        this.fetchCarGuarantees(carId);
        await this.fetchCarImages(carId);
        await this.fetchCarVideo(carId);
        this.initAllSwaggerImages();
    },
    beforeDestroy() {
        window.removeEventListener('message', this.handleIframeMessage);
    },
    methods: {
        initAllSwaggerImages() {
            let _this = this;
            let result = [];
            if (this.carVideo && this.carVideo.length > 0) {
                result.push({
                    url: this.carVideo[0],
                    type: this.isYoutubeVideo(this.carVideo[0]) ? 'youtube' : 'video'
                });
            }
            this.allSwaggerImages = result.concat(this.carImages.map(img => {
                return {
                    url: img,
                    type: 'image'
                }
            }));

            setTimeout(() => {
				$('.swiper-container')
                .slick({})
                .on('afterChange', function(slick, currentSlide){
                    _this.currentImage = currentSlide.currentSlide;
                    _this.currentMediaIndex = currentSlide.currentSlide;
                  });
				  $('.swiper_image').each(function(index) {
                    new Viewer(this, {
                        inline: false,
                        viewed() {
                            viewer.zoomTo(1);
                        }
                    });
                  });
              })
          },
        handleIframeMessage(event) {
            // 处理来自iframe的消息
            if (event.data && (event.data.type === 'iframe-loaded' || event.data.type === 'iframe-resized')) {
                // 查找所有iframe，确定是哪个发送的消息
                const iframes = document.querySelectorAll('iframe.content-frame');
                iframes.forEach(iframe => {
                    try {
                        if (iframe.contentWindow === event.source) {
                            // 更新对应iframe的高度
                            iframe.style.height = `${event.data.height}px`;
                        }
                    } catch (e) {
                        console.error('处理iframe消息出错', e);
                    }
                });
            }
        },
        setupAllContentFrames() {
            // 获取页面上所有content-frame类的iframe
            const contentFrames = document.querySelectorAll('iframe.content-frame');
            contentFrames.forEach(frame => {
                frame.onload = () => {
                    this.adjustIframeHeight(frame);
                };

                // 如果iframe已经加载完成，手动触发onload事件
                if (frame.contentDocument && frame.contentDocument.readyState === 'complete') {
                    frame.onload();
                }
            });
        },
        adjustIframeHeight(frame) {
            try {
                const frameDoc = frame.contentWindow.document;
                const frameBody = frameDoc.body;
                
                if (!frameBody) return;
                
                // 获取内容高度，添加一些边距
                const contentHeight = Math.max(
                    frameBody.scrollHeight,
                    frameBody.offsetHeight,
                    frameDoc.documentElement.scrollHeight,
                    frameDoc.documentElement.offsetHeight
                );
                
                // 设置iframe高度，添加20px的缓冲
                frame.style.height = (contentHeight + 20) + 'px';
                
                // 监听内容变化
                this.setupIframeResizeObserver(frame);
                
            } catch (e) {
                console.error('调整iframe高度失败', e);
                // 如果无法访问iframe内容，设置一个默认高度
                frame.style.height = '300px';
            }
        },
        setupIframeResizeObserver(frame) {
            try {
                const frameDoc = frame.contentWindow.document;
                const frameBody = frameDoc.body;
                
                if (!frameBody) return;
                
                // 使用MutationObserver监听内容变化
                const observer = new MutationObserver(() => {
                    this.adjustIframeHeight(frame);
                });
                
                // 监听所有变化
                observer.observe(frameBody, {
                    childList: true,
                    subtree: true,
                    attributes: true,
                    characterData: true
                });
                
                // 监听图片加载
                const images = frameDoc.querySelectorAll('img');
                images.forEach(img => {
                    img.addEventListener('load', () => {
                        this.adjustIframeHeight(frame);
                    });
                });
                
            } catch (e) {
                console.warn('无法设置iframe内容监听', e);
            }
        },
        resizeContentFrame(frameRef) {
            // 等待iframe加载完成后调整高度
            const frame = this.$refs[frameRef];
            if (frame) {
                frame.onload = () => {
                    this.adjustIframeHeight(frame);
                };
                
                // 如果iframe已经加载完成，立即调整高度
                if (frame.contentDocument && frame.contentDocument.readyState === 'complete') {
                    this.adjustIframeHeight(frame);
                }
            }
        },
        getHtmlContent(content) {
            // 准备一个完整的HTML文档结构，包含样式隔离
            return `
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="utf-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1">
                    <style>
                        * {
                            box-sizing: border-box;
                        }
                        body {
                            font-family: 'Microsoft YaHei', Arial, sans-serif;
                            line-height: 1.6;
                            color: #333;
                            padding: 15px;
                            margin: 0;
                            background: transparent;
                            overflow-x: hidden;
                        }
                        img {
                            max-width: 100%;
                            height: auto;
                            display: block;
                            margin: 10px 0;
                        }
                        table {
                            border-collapse: collapse;
                            width: 100%;
                            margin: 10px 0;
                        }
                        td, th {
                            border: 1px solid #ddd;
                            padding: 8px;
                            text-align: left;
                        }
                        th {
                            background-color: #f5f5f5;
                            font-weight: bold;
                        }
                        p {
                            margin: 10px 0;
                            line-height: 1.8;
                        }
                        h1, h2, h3, h4, h5, h6 {
                            margin: 15px 0 10px 0;
                            color: #5ACFC9;
                        }
                        ul, ol {
                            margin: 10px 0;
                            padding-left: 20px;
                        }
                        li {
                            margin: 5px 0;
                        }
                        blockquote {
                            margin: 15px 0;
                            padding: 10px 15px;
                            background-color: #f8f9fa;
                            border-left: 4px solid #5ACFC9;
                        }
                        code {
                            background-color: #f8f9fa;
                            padding: 2px 4px;
                            border-radius: 3px;
                            font-family: 'Courier New', monospace;
                        }
                        pre {
                            background-color: #f8f9fa;
                            padding: 15px;
                            border-radius: 5px;
                            overflow-x: auto;
                            margin: 10px 0;
                        }
                        a {
                            color: #5ACFC9;
                            text-decoration: none;
                        }
                        a:hover {
                            text-decoration: underline;
                        }
                    </style>
                    <script>
                        // 在加载完成后通知父页面更新高度
                        function notifyParent() {
                            if(window.parent && window.parent.postMessage) {
                                const height = Math.max(
                                    document.body.scrollHeight,
                                    document.body.offsetHeight,
                                    document.documentElement.scrollHeight,
                                    document.documentElement.offsetHeight
                                );
                                window.parent.postMessage({
                                    type: 'iframe-loaded',
                                    height: height
                                }, '*');
                            }
                        }
                        
                        window.addEventListener('load', function() {
                            notifyParent();
                            
                            // 监听内容变化
                            const observer = new MutationObserver(function() {
                                setTimeout(notifyParent, 100); // 延迟通知，确保DOM更新完成
                            });
                            
                            // 监听body的所有变化，包括子元素
                            observer.observe(document.body, {
                                attributes: true,
                                childList: true,
                                subtree: true,
                                characterData: true
                            });
                            
                            // 监听图片加载完成事件
                            document.querySelectorAll('img').forEach(img => {
                                img.addEventListener('load', function() {
                                    setTimeout(notifyParent, 100);
                                });
                                img.addEventListener('error', function() {
                                    setTimeout(notifyParent, 100);
                                });
                            });
                        });
                        
                        // 如果页面已经加载完成，立即通知
                        if (document.readyState === 'complete') {
                            setTimeout(notifyParent, 100);
                        }
                    </script>
                </head>
                <body>
                    ${content || '<p>暂无内容</p>'}
                </body>
                </html>
            `;
        },
        async fetchCarImages(carId) {
            this.carImages = images;
            console.log('获取车辆图片成功:', this.carImages);
        },
        async fetchCarVideo(carId) {
            this.carVideo = videos;
            console.log('获取车辆视频成功:', this.carVideo);
        },
        fetchCarEquipments(carId) {
            let _this = this;
            axios.get(`/api/equipment/${carId}`)  // 替换为实际后端接口地址
                .then(response => {
                    // 成功获取数据后赋值给data属性
                    // this.carInfo = response.data.carInfo
                    _this.carEquipments = response.data.data;
                    console.log('获取车辆配备成功:', _this.carEquipments);
                })
                .catch(error => {
                    console.error('获取车辆配备失败:', error)
                    // 可添加错误提示逻辑（如显示错误信息）
                })
        },
        // 获取卖家保证
        fetchCarGuarantees(carId) {
            let _this = this;
            axios.get(`/api/guarantee/${carId}`)  // 替换为实际后端接口地址
                .then(response => {
                    // 成功获取数据后赋值给data属性
                    // this.carInfo = response.data.carInfo
                    _this.carGuarantees = response.data.data;
                    console.log('获取车辆卖家保证成功:', _this.carGuarantees);
                })
                .catch(error => {
                    console.error('获取车辆卖家保证失败:', error)
                    // 可添加错误提示逻辑（如显示错误信息）
                })
        },
        // 获取经销商信息
        fetchDealerInfo(garageId) {
            console.log('获取经销商信息', garageId);
            let _this = this;
            axios.get(`/api/dealer/${garageId}`)  // 替换为实际后端接口地址
                .then(response => {
                    // 使用 Object.assign 来确保响应式更新
                    _this.carInfo = Object.assign({}, _this.carInfo, {
                        dealer: response.data.data
                    });
                    
                    if (_this.carInfo.dealer.photos && _this.carInfo.dealer.photos.length > 0) {
                        _this.carInfo.dealer.coverImage = _this.carInfo.dealer.photos[0];
                    }

                    document.title = _this.carInfo.dealer.dealerName + '/' + _this.carInfo.saleTitle;
                    // 添加或更新 Open Graph 标签
                    const ogTitle = document.querySelector('meta[property="og:title"]');
                    if (ogTitle) {
                        ogTitle.setAttribute('content', document.title);
                    } else {
                        const meta = document.createElement('meta');
                        meta.setAttribute('property', 'og:title');
                        meta.content = document.title;
                        document.getElementsByTagName('head')[0].appendChild(meta);
                    }
                    console.log('获取经销商信息成功:', _this.carInfo.dealer);
                })
                .catch(error => {
                    console.error('获取经销商信息失败:', error)
                    // 可添加错误提示逻辑（如显示错误信息）
                })
        },
        // 新增：定义获取车辆详情的请求方法
        fetchCarBaseInfo(carId) {
            let _this = this;
            _this.carInfo = car;
            _this.carInfo.specs = [
                { name: '所在城市', value: car.locationName || '--' },
                { name: '出廠日期', value: car.manufactureYear || '--' },
                { name: '里程數', value: car.mileage || '--' },
                { name: '引擎燃料', value: car.fuelSystem || '--' },
                { name: '排氣量', value: car.displacement || '--' },
                { name: '變速系統', value: car.transmission || '--' },
                { name: '車色', value: car.color || '--' },
                { name: '車門數', value: car.doorCount || '--' },
                { name: '乘坐人數', value: car.passengerCount || '--' },
                { name: '汽車品牌', value: car.brand || '--' },
                { name: '汽車型號', value: car.customModel || '--' },
            ];
            _this.carInfo.title = car.brand + ' ' + car.customModel + ' ' + car.manufactureYear;
            _this.carInfo.price = car.salePrice;
            _this.carInfo.dealer = {

            };
            // 这里将网页标题设置为title
            // document.title = _this.carInfo.title;
            console.log(_this.carInfo.specs);
            _this.fetchDealerInfo(carId);
        },
        changeMedia(index) {
			this.currentMediaIndex = index;
			$('.swiper-container').slick('slickGoTo', index);
        },
        getThumbnailSrc(media) {
            if (media.type === 'youtube') {
                //这里改为项目中的youtube.png
                return '/img/Youtube.png';
            } else if (media.type === 'video') {
                return '/img/video.png';
            }
            return media.url;
        },
        callPhone() {
            location.href = `tel:${this.carInfo.dealer.companyPhone}`;
        },
        contactLine() {
            location.href = `line://ti/p/${this.carInfo.dealer.lineId}`;
            setTimeout(() => {
                location.href = `https://line.me/R/ti/p/${this.carInfo.dealer.lineId}`;
            }, 1000);
        },
        isYoutubeVideo(url) {
            return url && (url.includes('youtube.com') || url.includes('youtu.be'));
        },
        convertToEmbedUrl(url) {
            if (url.includes('youtube.com/watch?v=')) {
                return url.replace('watch?v=', 'embed/');
            }
            if (url.includes('youtu.be/')) {
                return url.replace('youtu.be/', 'youtube.com/embed/');
            }
            return url;
        },
        getImageOnlyIndex(index) {
            // 获取纯图片列表中的索引
            const imageList = this.allSwaggerImages.filter(media => media.type === 'image');
            return imageList.findIndex(media => media === this.allSwaggerImages[index]);
        }
    },
    mounted() {
        // 初始化纯图片列表
        this.imageOnlyList = this.allSwaggerImages.filter(media => media.type === 'image');
    }
});