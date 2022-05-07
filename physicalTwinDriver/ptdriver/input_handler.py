from redis import Redis
import time

from .braccio import Braccio
from .utils import decode_dict


sleep_time_in_seconds = 5


def next_command(dl: Redis):
    last_key = dl.zrange("PTCommand_UNPROCESSED", start=0, end=0)
    if last_key:
        return last_key[0]
    else:
        return None


def input_handler(robot: Braccio, dl: Redis, status: dict):
    while not status["quit"]:

        command_key = None
        if not status["command"]:
            command_key = next_command(dl)

        if command_key:
            command = decode_dict(dl.hgetall(command_key))
            if status["twinId"] == command["twinId"]:
                name, args, command_id = command["name"], command["arguments"], command["commandId"]
                command_line = f"{name} {args}"
                print(f"IN  << {command_line}")
                robot.write(command_line)
                status["command"] = command
                command["whenProcessed"] = status["timestamp"]

                # Move to the processed commands list
                dl.zrem("PTCommand_UNPROCESSED", command_key)
                dl.zadd("PTCommand_PROCESSED", {command_key: command_id})
                dl.hset(command_key, mapping=command)
            else:
                pass  # This command is not for this twin
        else:
            time.sleep(sleep_time_in_seconds)