import request from '@/utils/request'

export function dailytest(userId) {
  return request({
    url: '/wordtest/daily',
    method: 'get',
    params: { userId}
  })
}

export function testreport(data) {
  return request({
    url: '/wordtest/report',
    method: 'post',
    data
  })
}

export function getUserInfo(username) {
  return request({
    url: '/user/userinfo',
    method: 'get',
    params: { username }
  })
}