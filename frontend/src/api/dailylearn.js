import request from '@/utils/request'

export function dailylearnfirst(data) {
  return request({
    url: '/wordStudy/getStudyWordsFirst',
    method: 'post',
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    data: new URLSearchParams(data)
  })
}

export function dailylearn(data) {
  return request({
    url: '/wordStudy/getStudyWords',
    method: 'post',
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    data: new URLSearchParams(data)
  })
}

export function getNextWord({ username, word, date, action }) {
  return request({
    url: '/wordStudy/getNextWord',
    method: 'put',
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    data: new URLSearchParams({
      username,
      word,
      date,
      action
    })
  })
}