# DSH Android

设备端 DeepSeek Harness（DSH）智能体安卓壳。

**目标**：安卓设备上独立运行全部 Agent 能力，前端一比一复刻 DSH 的界面与交互逻辑，后端引擎参考 Open Minis 的移动端路线（原生壳 + iSH/PRoot 设备端 Linux + Node 运行时 + 原生能力桥）。

## 当前版本（v0.1.0）已实现

- Kotlin 原生壳 + WebView 加载 DSH 风格前端（对话 / 会话 / 插件 / Agent / 设置面板，含工具调用流式渲染）
- 原生能力桥 `window.DSHNative`：设备信息、剪贴板、通知、引擎控制
- 引擎宿主架构 `EngineHost`：DEMO / REMOTE / LOCAL 三模式
- 前端内置 demo 引擎，用于无后端时验证 UI 与工具流式渲染
- GitHub Actions 云构建 APK

## P1 路线（后续迭代）

- 打包设备端 Node 运行时（nodejs-mobile）+ `@deepseek-ai/dsh` 引擎 bundle
- iSH(Android 用 PRoot) 设备端 Linux 沙箱 + Node host
- 原生能力封装为 MCP Server，经官方 MCP 客户端插件接入
- 浏览器 / 终端 / 沙箱等系统能力接入

## 许可证

DSH 为 MIT；Open Minis 仅作架构参考（GPLv3），本项目不复用其代码。
