import request from '@/utils/request'

export function getdialogue(dialogueId) {
  return request({
    url: `/dialogue/records/${dialogueId}`,
    method: 'get'
  })
}

export function getdialoguefirst(userId) {
  return request({
    url: `/dialogue/user-records/${userId}`,
    method: 'get'
  })
}

// 新增：创建对话
export function createDialogue(data) {
  return request({
    url: '/dialogue/create',
    method: 'post',
    data
  })
}

export function Dialoguerecord(data) {
  return request({
    url: '/dialogue/record',
    method: 'post',
    data
  })
}
