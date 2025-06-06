import request from '@/utils/request'

export function sendVerificationCode(data) {
  return request({
    url: '/sendmail',
    method: 'post',
    data
  })
}


