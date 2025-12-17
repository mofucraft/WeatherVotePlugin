# Weather vote plugin
現在の最新バージョンは**1.1.3**です。

The current latest version is **1.1.3**.
# Overview
Weather vote plugin は プレイヤーの投票により、晴れまたは昼にするSpigotプラグインです。

※もふくらふとに最適化して作られたプラグインです。

・反対者が一人いた場合にはそのままの気候・時間

・ワールド「world」のみ晴れ・昼に変更

・投票受付中には投票を開始させることはできません。（二重投票開始をしない）

・5分おきに投票できます

Weather vote plugin is a Spigot plugin that turns sunny or daytime according to the player's vote.

※This is a plugin optimized for "MOFUCRAFT Server".

・If even one person opposes the climate/time change, it will not change.

・Only world "world" changed to sunny/daytime

・No new ballots can be initiated while voting is open.

・You can vote every 5 minutes

# Commands

| コマンド                      |        エイリアス        |       説明        | 権限      |
|---------------------------|:-------------------:|:---------------:|---------|
| /mvote sun                |                     | 天候を晴れにする投票を開始する | default |
| /mvote day                |                     | 時間を昼にする投票を開始する  | default |
| /mvote oppose <sun / day> | mvote o <sun / day> |     投票に反対する     | default |
| /mvote voteinfo           |     mvote info      |   現在の投票の状態を見る   | default |
| /mvote version            |                     |   現在のバージョンを見る   | default |

| Commands                  |        Alias        |            Description            | Permission |
|---------------------------|:-------------------:|:---------------------------------:|------------|
| /mvote sun                |                     | Start voting to clear the weather | default    |
| /mvote day                |                     |    Start voting to set daytime    | default    |
| /mvote oppose <sun / day> | mvote o <sun / day> |           Vote against            | default    |
| /mvote voteinfo           |     mvote info      |    View current voting status     | default    |
| /mvote version            |                     |       View current version        | default    |

# Language
以下の言語に対応しています。

| 言語  |     Permission     |
|:---:|:------------------:|
| 日本語 |                    |
| 英語  | mofucraft.english  |


The following languages are supported.

| Language |     Permission     |
|:--------:|:------------------:|
| Japanese |                    |
| English  | mofucraft.english  |

# ライセンス (Licence)

このコードはMIT Lisenceの元で公開されています。  
This code is published under the MIT License.

[Apache License 2.0 でライセンスされたコードが含まれています。](https://github.com/mofucraft/WeatherVotePlugin/blob/main/src/main/java/ine0056/github/com/weathervoteplugin/MessageManager.java)  
[Contains code licensed under the Apache License 2.0.](https://github.com/mofucraft/WeatherVotePlugin/blob/main/src/main/java/ine0056/github/com/weathervoteplugin/MessageManager.java)