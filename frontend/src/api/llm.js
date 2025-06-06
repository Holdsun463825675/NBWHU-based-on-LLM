import request from '@/utils/request'

export function contextualreading(data) {
  return request({
    url: '/llm/contextual_reading',
    method: 'post',
    data
  })
}

export function situationaldialogue(data) {
  return request({
    url: '/llm/situational_dialogue',
    method: 'post',
    data
  })
}

export function semanticwordexploration(data) {
  return request({
    url: '/llm/semantic_word_exploration',
    method: 'post',
    data
  })
}