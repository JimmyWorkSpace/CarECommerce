-- 测试广告数据
INSERT INTO car_advertisement (imageUrl, linkUrl, isLink, content, title, showOrder, delFlag, createTime) VALUES
('/img/car/car4.jpg', 'https://example.com/inspection', 1, '所有车辆均经过专业检测，确保车况良好', '专业检测', 1, 0, NOW()),
('/img/car/car6.jpg', 'https://example.com/guarantee', 1, '提供完善的售后保障，让您购车无忧', '品质保证', 2, 0, NOW()),
('/img/car/car8.jpg', '', 0, '<h2>精选优质二手车</h2><p>我们精心挑选每一辆二手车，确保车况良好，价格实惠。</p><ul><li>专业检测团队</li><li>价格透明公开</li><li>售后保障完善</li><li>多种车型选择</li></ul><p>让您轻松找到心仪的座驾！</p>', '精选好车', 3, 0, NOW()),
('/img/car/car9.jpg', 'https://example.com/service', 1, '专业团队为您提供全程购车服务', '专业服务', 4, 0, NOW()); 