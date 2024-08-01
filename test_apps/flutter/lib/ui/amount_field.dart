import 'package:flutter/material.dart';

class AmountField extends StatelessWidget {
  AmountField({Key? key, required this.flex, required this.amount})
      : super(key: key);

  String amount;
  final int flex;

  @override
  Widget build(BuildContext context) {
    return Expanded(
        flex: flex,
        child: Row(mainAxisAlignment: MainAxisAlignment.end, children: [
          Text(
            "$amount",
            style: const TextStyle(fontSize: 26),
          )
        ]));
  }
}
