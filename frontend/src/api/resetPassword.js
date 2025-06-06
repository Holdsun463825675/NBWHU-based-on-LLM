import request from '@/utils/request'

export function resetPassword(data) {
  return request({
    url: '/auth/resetpassword',
    method: 'post',
    data
  })
}


