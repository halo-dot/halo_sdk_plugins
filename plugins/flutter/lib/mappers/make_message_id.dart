import '../model/halo_ui_message_id.dart';

HaloUiMessageId makeMessageId(String value) {
  switch (value) {
    case 'PresentCard':
      return HaloUiMessageId.presentCard;
    case 'Processing':
      return HaloUiMessageId.processing;
    case 'CardReadOK_RemoveCard':
      return HaloUiMessageId.cardReadOK_RemoveCard;
    case 'SeePhoneForInstructions_ThenTapAgain':
      return HaloUiMessageId.seePhoneForInstructions_ThenTapAgain;
    case 'AuthorisingWait':
      return HaloUiMessageId.authorisingWait;
    case 'TryAgain':
      return HaloUiMessageId.tryAgain;
    case 'TryAnotherCard':
      return HaloUiMessageId.tryAnotherCard;
    default:
      throw Exception('Unknown HaloUiMessageId value: $value');
  }
}
