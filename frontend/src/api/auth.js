import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/login',
    method: 'post',
    data
  })
}

export function learnedword(userId,date) {
  return request({
    url: '/user/learnedNumber',
    method: 'get',
    params: { userId,date}
  })
}