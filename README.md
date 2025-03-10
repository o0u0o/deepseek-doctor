# DeepSeek+SpringAI 家庭医生


## 技术架构

SpringBoot相关版本[重要]：
- SpringBoot 3.4.2
- OpenJDK 21

数据库选型：MySQL8

## 对接大模型
使用REST API接口通过Ollama调用大模型
```
curl http://localhost:11434/api/generate -d '{
  "model": "deepseek-r1:1.5b",
  "prompt":"你是谁?"
}'
```
![访问结果](https://public-images-1252032054.cos.ap-guangzhou.myqcloud.com/public%2FSnipaste_2025-03-10_22-34-09.png)


## SpringAI
Spring AI 是一个面向 AI 工程的应用框架，其目标是将 Spring 生态系统的可移植性和模块化设计等设计原则应用到 AI 领域，并推动将 POJO 作为应用的构建块应用于 AI 领域。