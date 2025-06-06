import request from '@/utils/request'

// 查询单词列表（模糊搜索，分页）
export function searchWords(keyword, page, size) {
  return request({
    url: '/word/wordsearch',
    method: 'get',
    params: {
      keyword,
      page,
      size
    }
  })
}


// 获取单词详情
export function getWordDetail(id) {
  return request({
    url: `/word/${id}`,
    method: 'get'
  })
}

export function exam(word) {
  return request({
    url: '/word/getWordsInfo',
    method: 'post',
    data: word,
    headers: { 'Content-Type': 'text/plain' } // 强制用纯文本
  })
}

export function getWordInfo(word) {
  return request({
    url: '/word/getWordInfo',
    method: 'get',
    params: { word }
  })
}

