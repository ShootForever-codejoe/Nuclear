import websockets
from asyncio import run as run_async, Future
from json import dumps, loads

users = []
FREE = False

async def remove_user(javax.websocket):
    name = ""
    for user in users:
        if (user["javax.websocket"] == javax.websocket):
            name = user["name"]
            users.remove(user)
            break
    if (name == ""):
        return
    for user in users:
        await user["javax.websocket"].send(dumps({
            "func": "remove_user",
           "name": name
        }))


async def handler(javax.websocket, path):
    try:
        async for message in javax.websocket:
            print(f"ip: {javax.websocket.remote_address[0]}")
            print(f"message: {message}")
            print()
            messageJson = loads(message)
            if (messageJson["func"] == "create_user"):
                for user in users:
                    if (user["server"] == messageJson["server"]):
                        await user["javax.websocket"].send(dumps({
                            "func": "create_user",
                            "name": messageJson["name"]
                        }))
                        await javax.websocket.send(dumps({
                            "func": "create_user",
                            "name": user["name"]
                        }))
                users.append({
                    "server": messageJson["server"],
                    "name": messageJson["name"],
                    "javax.websocket": javax.websocket
                })
            
            elif (messageJson["func"] == "remove_user"):
                remove_user(javax.websocket)

            elif (messageJson["func"] == "send_msg"):
                for user in users:
                    await user["javax.websocket"].send(dumps({
                        "func": "send_msg",
                        "name": user["name"],
                        "msg": messageJson["msg"]
                    }))

            elif (messageJson["func"] == "get_free"):
                await javax.websocket.send(dumps({
                    "func": "get_free",
                    "free": FREE
                }))

    except websockets.ConnectionClosed:
        remove_user(javax.websocket)
        
async def main():
    async with websockets.serve(handler, "0.0.0.0", 12345):
        print("Server Started")
        print()
        await Future()

if (__name__ == '__main__'):
    run_async(main())
