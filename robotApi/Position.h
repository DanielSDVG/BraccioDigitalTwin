/*
    File: Position.h

    Copyright 2018 Stefan Strömberg, stefangs@nethome.nu

    Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
    in compliance with the License. You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software distributed under the License
    is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
    either express or implied. See the License for the specific language governing permissions and
    limitations under the License.
*/

#ifndef POSITION_H_
#define POSITION_H_

#define GRIPPER_OPEN 10
#define GRIPPER_CLOSED 73

class Position
{
    public:
    
        Position& setBase(int b);
        Position& setShoulder(int s);
        Position& setElbow(int e);
        Position& setWrist(int v);
        Position& setWristRotation(int w);
        Position& setGripper(int g);
        Position& set(int basePos, int shoulderPos, int elbowPos, int wristPos, int wristRotationPos, int gripperPos);
        Position& set(int i, int pos);

        inline int getBase() const { return base; }
        inline int getShoulder() const { return shoulder; }
        inline int getElbow() const { return elbow; }
        inline int getWrist() const { return wrist; }
        inline int getWristRotation() const { return wristRotation; }
        inline int getGripper() const { return gripper; }
        int get(int i) const;

        Position();
        Position(int basePos, int ShoulderPos, int elbowPos, int wristPos, int wristRotationPos, int gripperPos);

        int maxPositionDiff(const Position& p) const;
        int setFromString(char* string);

        Position& operator=(const Position& p);
        bool operator==(const Position& rhs);
        bool operator!=(const Position& rhs);

    private:

        int base;
        int shoulder;
        int elbow;
        int wrist;
        int wristRotation;
        int gripper;
        int limit(int value, int minv, int maxv);
        int parseInt(char *&in, bool &isSuccess);
        
};

extern int minAngles[6];
extern int maxAngles[6];

#endif
