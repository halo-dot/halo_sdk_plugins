import 'package:flutter/material.dart';

class Keypad extends StatelessWidget {
  Keypad(
      {Key? key,
      required this.flex,
      required this.setAmount,
      required this.amount})
      : super(key: key);

  final int flex;
  final Function setAmount;
  String amount;

  void _onPress(String text) {
    if (text == "C") {
      setAmount("");
    } else {
      setAmount(amount += text);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Expanded(
        flex: flex,
        child: Column(mainAxisSize: MainAxisSize.max, children: [
          Expanded(
              child: Row(
            mainAxisAlignment: MainAxisAlignment.spaceEvenly,
            children: [
              KeypadButton(
                text: "1",
                onTap: _onPress,
                key: const Key("1"),
              ),
              KeypadButton(text: "2", onTap: _onPress, key: const Key("2")),
              KeypadButton(text: "3", onTap: _onPress, key: const Key("3")),
            ],
          )),
          Expanded(
              child: Row(
            mainAxisAlignment: MainAxisAlignment.spaceEvenly,
            children: [
              KeypadButton(text: "4", onTap: _onPress, key: const Key("4")),
              KeypadButton(text: "5", onTap: _onPress, key: const Key("5")),
              KeypadButton(text: "6", onTap: _onPress, key: const Key("6")),
            ],
          )),
          Expanded(
              child: Row(
            mainAxisAlignment: MainAxisAlignment.spaceEvenly,
            children: [
              KeypadButton(text: "7", onTap: _onPress, key: const Key("7")),
              KeypadButton(text: "8", onTap: _onPress, key: const Key("8")),
              KeypadButton(text: "9", onTap: _onPress, key: const Key("9")),
            ],
          )),
          Expanded(
              child: Row(
            mainAxisAlignment: MainAxisAlignment.spaceEvenly,
            children: [
              KeypadButton(text: ".", onTap: _onPress, key: const Key(".")),
              KeypadButton(text: "0", onTap: _onPress, key: const Key("0")),
              KeypadButton(text: "C", onTap: _onPress, key: const Key("C")),
            ],
          )),
        ]));
  }
}

class KeypadButton extends StatelessWidget {
  const KeypadButton({Key? key, required this.text, required this.onTap})
      : super(key: key);

  final String text;
  final Function onTap;

  @override
  Widget build(BuildContext context) {
    return (Expanded(
        flex: 1,
        child: InkWell(
          child: Expanded(
            child: Center(
              child: Text(
                text,
                style: const TextStyle(fontSize: 22),
              ),
            ),
          ),
          onTap: () => onTap(text),
          highlightColor: Colors.lightBlue,
        )));
  }
}
