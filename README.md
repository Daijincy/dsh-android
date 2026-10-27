# DSH Android

设备端 DeepSeek Harness（DSH）安卓客户端。

**目标**：安卓上体验与官方 DeepSeek Harness 原版一致的 Agent —— 界面就是官方原版前端，功能是真实引擎，交互与原版相同。完整形态为设备端独立运行全部 Agent 能力。

## 当前版本（v0.2.0）—— 真 DSH 客户端

- **原版界面**：直接加载真实 `dsh --profile web` 主机，UI 即官方 DeepSeek Harness 前端（非自绘壳）。
- **真实功能**：连接真实 DSH 引擎，Agent 对话 / 工具调用 / 会话等与官方一致。
- **连接页**：首次启动填写主机地址（含 `?token=`），持久化后加载。
- **原生桥**：设备信息、剪贴板（P1 扩展为 MCP 能力）。

### 使用
1. 在电脑/服务器启动主机：`npm i -g @deepseek-ai/dsh && dsh --profile web --no-open`
2. 把打印出的 `http://<host>:<port>/?token=...` 填入 App 连接页（同一网络）。
3. 连接后即为官方原版界面与真实功能。

## P1 路线（设备端独立引擎）

- 打包 Node-for-Android（nodejs-mobile）+ `@deepseek-ai/dsh` 引擎 bundle
- 设备端本地运行 `dsh --profile web`，WebView 直连 localhost（真正独立、离线可用）
- 原生能力封装为 MCP Server 接入官方 MCP 客户端插件
- 已确认：官方前端 `@deepseek-ai/dsh-web-frontend`（4.6MB dist）与引擎 `@deepseek-ai/dsh` 均可从 npm 获取；`dsh --profile web` 一体化伺服前端 + `/api`（本地已验证）

## 许可证

DSH 为 MIT（注意 npm 包声明 BSD-3-Clause）；Open Minis 仅作架构参考（GPLv3），本项目不复用其代码。
