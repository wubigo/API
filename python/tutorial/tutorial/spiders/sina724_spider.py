import scrapy


class Sina724Spider(scrapy.Spider):
    name = 'sina724'
    start_urls = [
        'http://finance.sina.com.cn/7x24/',
    ]

    def parse(self, response):
        for quote in response.css('div.bd_list'):
            yield {
                'text': quote.css('div.bd_i_c bd_i_txt::text').get(),

            }

