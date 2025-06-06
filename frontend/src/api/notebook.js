import request from '@/utils/request'

// 获取单词详情
export function getnotebook(id) {
  return request({
    url: `/wordRecord/get/${id}`,
    method: 'get'
  })
}

export function addnotebook({ userId, wordId }) {
  return request({
    url: '/wordRecord/add',
    method: 'post',
    params: { userId, wordId }
  })
}

export function deletenotebook({ userId, wordId }) {
  return request({
    url: '/wordRecord/delete',
    method: 'delete',
    params: { userId, wordId }
  })
}