# Weather vote plugin
現在の最新バージョンは**1.0.4**です。
# Overview
Weather vote plugin は プレイヤーの投票により、晴れまたは昼にするSpigotプラグインです。

※もふくらふとに最適化して作られたプラグインです。

・反対者が一人いた場合にはそのままの気候・時間

・ワールド「world」のみ晴れ・昼に変更

・投票受付中には投票を開始させることはできません。（二重投票開始をしない）

・5分おきに投票できます

# Commands

| コマンド | エイリアス | 説明 | 権限 |
| --- | :---: | :---: | --- |
|/mvote sun|  |天候を晴れにする投票を開始する|default|
|/mvote day| |時間を昼にする投票を開始する|default|
|/mvote oppose <sun / day>| mvote o <sun / day> |投票に反対する|default|
|/mvote voteinfo| mvote info |現在の投票の状態を見る|default|
|/mvote version| |現在のバージョンを見る|default|
